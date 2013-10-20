package com.morgan.design.adaptor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundGroupManagementActivity;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.helpers.Logger;

public class GroupAdaptor extends ArrayAdapter<BrewGroup> {

	private final String LOG_TAG = "GroupAdapter";

	private final List<BrewGroup> groups;
	private final Activity context;

	public GroupAdaptor(final Activity context, final int textViewResourceId, final List<BrewGroup> groups) {
		super(context, textViewResourceId, groups);
		this.context = context;
		this.groups = groups;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			final LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.group_data_row, null);

			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name_text_view);
			holder.numberOfPlayers = (TextView) view.findViewById(R.id.number_of_players);
			holder.trashButton = (Button) view.findViewById(R.id.remove_group);

			view.setTag(holder);

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					view.showContextMenu();
				}
			});
		}
		else {
			holder = (ViewHolder) view.getTag();
		}

		holder.trashButton.setOnClickListener(new TrashClickHandler(position));

		final BrewGroup group = this.groups.get(position);
		Logger.d(this.LOG_TAG, group.getName());

		if (group != null) {
			final TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
			nameTextView.setText("Group Name: " + group.getName());

			final TextView ratingTextView = (TextView) view.findViewById(R.id.number_of_players);
			ratingTextView.setText("Number of Players: " + group.getSize());

			final ImageView playerSmileyIcon = (ImageView) view.findViewById(R.id.group_icon);
			playerSmileyIcon.setImageResource(R.drawable.add_group);
			playerSmileyIcon.setAdjustViewBounds(true);
			playerSmileyIcon.setPadding(1, 1, 1, 1);
		}

		return view;
	}

	class ViewHolder {
		TextView name;
		TextView numberOfPlayers;
		Button trashButton;
	}

	public class TrashClickHandler implements OnClickListener {
		private final int position;

		public TrashClickHandler(final int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			Logger.d(GroupAdaptor.this.LOG_TAG, "Trash clicked");

			Logger.d(GroupAdaptor.this.LOG_TAG, "Trash clicked");
			final BrewGroup group = GroupAdaptor.this.groups.get(this.position);
			shortToast("Removed group: " + group.getName());
			GroupAdaptor.this.groups.remove(this.position);
			notifyDataSetChanged();
			final TeaRoundGroupManagementActivity activity = (TeaRoundGroupManagementActivity) GroupAdaptor.this.context;
			activity.removeGroup(group);
		}
	}

	public void shortToast(final CharSequence message) {
		Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
	}
}
