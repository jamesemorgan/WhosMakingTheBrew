package com.morgan.design.adaptor.refactor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.db.domain.PlayerStats;

public class BrewPlayerStatsExpandableListAdapter extends BaseExpandableListAdapter {

	public LayoutInflater inflater;

	private List<PlayerStats> playerStats;

	public BrewPlayerStatsExpandableListAdapter(List<PlayerStats> playerStats, Context context) {
		this.playerStats = playerStats;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setPlayerStats(List<PlayerStats> playerStats) {
		this.playerStats = playerStats;
		notifyDataSetChanged();
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.dashboard_brew_stats_player_details_list_child, null);
		}

		PlayerStats playerStat = playerStats.get(groupPosition);

		ChildViewHolder childView = new ChildViewHolder(view);
		childView.brewsEntered.setText("Brews Entered: " + playerStat.getTotalTimesRun());
		childView.brewsWon.setText("Brews Won: " + playerStat.getTotalTimesWon());
		childView.lowestScore.setText("Lowest Score: " + playerStat.getLowestScore());
		childView.highestScore.setText("Highest Score: " + playerStat.getHighestScore());

		return view;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.dashboard_brew_stats_player_name_list_parent, null);
		}

		PlayerStats playerStat = playerStats.get(groupPosition);

		ParentViewHolder parentView = new ParentViewHolder(view);
		parentView.playerName.setText(playerStat.getBrewPlayer().getName());

		return view;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return playerStats.get(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return playerStats.get(groupPosition).getBrewPlayer().getName();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public int getGroupCount() {
		return playerStats.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	class ChildViewHolder {
		TextView brewsEntered;
		TextView brewsWon;
		TextView highestScore;
		TextView lowestScore;

		public ChildViewHolder(View view) {
			brewsEntered = (TextView) view.findViewById(R.id.player_stats_brews_entered);
			brewsWon = (TextView) view.findViewById(R.id.player_stats_brews_won);
			highestScore = (TextView) view.findViewById(R.id.player_stats_highest_score);
			lowestScore = (TextView) view.findViewById(R.id.player_stats_lowest_score);
		}
	}

	class ParentViewHolder {
		TextView playerName;

		public ParentViewHolder(View view) {
			playerName = (TextView) view.findViewById(R.id.brew_player_name);
		}
	}
}
