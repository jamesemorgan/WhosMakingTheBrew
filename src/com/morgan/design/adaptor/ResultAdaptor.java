package com.morgan.design.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.utils.SmileyIconUtils;
import com.morgan.design.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ResultAdaptor extends ArrayAdapter<BrewPlayer> {

    private final Logger LOG = LoggerFactory.getLogger(ResultAdaptor.class);

    private final List<BrewPlayer> results;
    private final List<Integer> imageResources;
    private final Activity context;

    public ResultAdaptor(final Activity context, final int textViewResourceId, final List<BrewPlayer> results) {
        super(context, textViewResourceId, results);
        this.context = context;
        this.results = new ArrayList<BrewPlayer>(results);
        imageResources = SmileyIconUtils.determineIconList(this.results.size());
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            // we first inflate the XML layout file and retrieve reference of
            // the described View.
            final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.player_results_row, null);
        }

        final BrewPlayer player = results.get(position);
        LOG.debug(player.getName());

        view.setOnClickListener(new PlayerListClickHandler(position));

        if (player != null) {
            final TextView positionTextView = (TextView) view.findViewById(R.id.game_position);
            positionTextView.setText("" + (position + 1) + "");

            final TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
            nameTextView.setText("Name: " + player.getName());

            final TextView ratingTextView = (TextView) view.findViewById(R.id.score_text_view);
            ratingTextView.setText("Score: " + player.getScore());

            final ImageView playerSmileyIcon = (ImageView) view.findViewById(R.id.player_smiley_icon);
            playerSmileyIcon.setImageResource(imageResources.get(position));
            playerSmileyIcon.setAdjustViewBounds(true);
            playerSmileyIcon.setPadding(2, 2, 2, 2);
        }

        return view;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    public class PlayerListClickHandler implements OnClickListener {
        private final int playerPosition;

        public PlayerListClickHandler(final int position) {
            playerPosition = position;
        }

        @Override
        public void onClick(final View v) {
            LOG.debug("Trash clicked");
            final BrewPlayer player = results.get(playerPosition);

            if (0 == playerPosition) {
                shortToast(player.getName() + " is making the tea...!");
            } else {
                shortToast(player.getName() + " came " + (playerPosition + 1) + Utils.getSuffix(playerPosition + 1));
            }
        }
    }

    public void shortToast(final CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
