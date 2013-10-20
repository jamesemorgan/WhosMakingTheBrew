package com.morgan.design.adaptor;

import java.util.ArrayList;
import java.util.List;

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
import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.SmileyIconUtils;
import com.morgan.design.utils.Utils;

public class ResultAdaptor extends ArrayAdapter<BrewPlayer> {

	private final String LOG_TAG = "ResultsAdapter";

	private final List<BrewPlayer> results;
	private final List<Integer> imageResources;
	private final Activity context;

	public ResultAdaptor(final Activity context, final int textViewResourceId, final List<BrewPlayer> results) {
		super(context, textViewResourceId, results);
		this.context = context;
		this.results = new ArrayList<BrewPlayer>(results);
		this.imageResources = SmileyIconUtils.determineIconList(this.results.size());
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			// we first inflate the XML layout file and retrieve reference of
			// the described View.
			final LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.player_results_row, null);
		}

		final BrewPlayer player = this.results.get(position);
		Logger.d(this.LOG_TAG, player.getName());

		view.setOnClickListener(new PlayerListClickHandler(position));

		if (player != null) {
			final TextView positionTextView = (TextView) view.findViewById(R.id.game_position);
			positionTextView.setText("" + (position + 1) + "");

			final TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
			nameTextView.setText("Name: " + player.getName());

			final TextView ratingTextView = (TextView) view.findViewById(R.id.score_text_view);
			ratingTextView.setText("Score: " + player.getScore());

			final ImageView playerSmileyIcon = (ImageView) view.findViewById(R.id.player_smiley_icon);
			playerSmileyIcon.setImageResource(this.imageResources.get(position));
			playerSmileyIcon.setAdjustViewBounds(true);
			playerSmileyIcon.setPadding(2, 2, 2, 2);
		}

		return view;
	}

	@Override
	public int getCount() {
		return this.results.size();
	}

	public class PlayerListClickHandler implements OnClickListener {
		private final int playerPosition;

		public PlayerListClickHandler(final int position) {
			this.playerPosition = position;
		}

		@Override
		public void onClick(final View v) {
			Logger.d(ResultAdaptor.this.LOG_TAG, "Trash clicked");
			final BrewPlayer player = ResultAdaptor.this.results.get(this.playerPosition);

			if (0 == this.playerPosition) {
				shortToast(player.getName() + " is making the tea...!");
			}
			else {
				shortToast(player.getName() + " came " + (this.playerPosition + 1)
						+ Utils.getSuffix(this.playerPosition + 1));
			}
		}
	}

	public void shortToast(final CharSequence message) {
		Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
	}
}
