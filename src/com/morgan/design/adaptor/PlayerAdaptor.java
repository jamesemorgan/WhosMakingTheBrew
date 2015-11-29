package com.morgan.design.adaptor;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundHomeActivity.TrashClickHandler;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.utils.SmileyIconUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerAdaptor extends ArrayAdapter<BrewPlayer> {

    private final Logger LOG = LoggerFactory.getLogger(PlayerAdaptor.class);

    private static final int PLAYER_DATA_ROW = R.layout.player_data_row;

    private final List<BrewPlayer> players;
    private final Activity context;

    private final TrashClickHandler trashClickHandlerListener;

    public PlayerAdaptor(final Activity context, final List<BrewPlayer> players, final TrashClickHandler trashClickHandlerListener) {
        super(context, PLAYER_DATA_ROW, players);
        this.context = context;
        this.players = players;
        this.trashClickHandlerListener = trashClickHandlerListener;
        setNotifyOnChange(true);
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            // We first inflate the XML layout file and retrieve reference of the described View.
            final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(PLAYER_DATA_ROW, null);

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.trashButton.setTag(position);
        holder.trashButton.setOnClickListener(trashClickHandlerListener);

        final BrewPlayer player = players.get(position);
        LOG.debug(player.getName());

        if (player != null) {
            holder.playerName.setText("Name: " + player.getName());
            holder.playerScore.setText("Score: " + player.getScore());
        }

        return view;
    }

    class ViewHolder {
        TextView playerName;
        TextView playerScore;
        ImageButton trashButton;
        ImageView playerSmileyIcon;

        public ViewHolder(View view) {
            playerName = (TextView) view.findViewById(R.id.player_data_player_name);
            playerScore = (TextView) view.findViewById(R.id.player_data_player_details);
            trashButton = (ImageButton) view.findViewById(R.id.player_data_remove_player);

            playerSmileyIcon = (ImageView) view.findViewById(R.id.player_data_player_icon);
            playerSmileyIcon.setImageResource(SmileyIconUtils.getDefaultSmiley());
            // playerSmileyIcon.setAdjustViewBounds(true);
        }
    }

}
