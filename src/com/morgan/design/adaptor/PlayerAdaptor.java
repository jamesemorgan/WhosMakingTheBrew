package com.morgan.design.adaptor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundGeneratorHomeActivity.TrashClickHandler;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.SmileyIconUtils;

public class PlayerAdaptor extends ArrayAdapter<BrewPlayer> {

	private final static String LOG_TAG = "PlayerAdapter";

	private final List<BrewPlayer> players;
	private final Activity context;

	private final TrashClickHandler trashClickHandlerListener;

	public PlayerAdaptor(final Activity context, final int textViewResourceId, final List<BrewPlayer> players,
			final TrashClickHandler trashClickHandlerListener) {
		super(context, textViewResourceId, players);
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
			// we first inflate the XML layout file and retrieve reference of
			// the described View.
			final LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.player_data_row, null);

			holder = new ViewHolder();
			holder.playerName = (TextView) view.findViewById(R.id.name_text_view);
			holder.playerScore = (TextView) view.findViewById(R.id.score_text_view);
			holder.trashButton = (Button) view.findViewById(R.id.remove_player);

			holder.playerSmileyIcon = (ImageView) view.findViewById(R.id.player_smiley_icon);
			holder.playerSmileyIcon.setImageResource(SmileyIconUtils.getDefaultSmiley());
			holder.playerSmileyIcon.setAdjustViewBounds(true);

			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}

		holder.trashButton.setTag(position);
		holder.trashButton.setOnClickListener(this.trashClickHandlerListener);

		final BrewPlayer player = this.players.get(position);
		Logger.d(LOG_TAG, player.getName());

		if (player != null) {
			final TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
			nameTextView.setText("Name: " + player.getName());

			final TextView ratingTextView = (TextView) view.findViewById(R.id.score_text_view);
			ratingTextView.setText("Score: " + player.getScore());
		}

		return view;
	}

	class ViewHolder {
		TextView playerName;
		TextView playerScore;
		Button trashButton;
		ImageView playerSmileyIcon;
	}

}
