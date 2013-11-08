package com.morgan.design.fragment.refactor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;

public class BrewStatsFragments extends Fragment {

	private static Logger LOG = LoggerFactory.getLogger(BrewStatsFragments.class);

	private BrewStats brewStats;
	private List<PlayerStats> playerStats;

	private ExpandableListAdapter adapter;

	public void setBrewStats(BrewStats brewStats) {
		this.brewStats = brewStats;
	}

	public void setPlayerStats(List<PlayerStats> playerStats) {
		this.playerStats = playerStats;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_stats, container, false);

		TextView totalRoundsRun = (TextView) rootView.findViewById(R.id.total_rounds_run);
		totalRoundsRun.setText("" + brewStats.getTotalTimesRun());

		TextView highestScore = (TextView) rootView.findViewById(R.id.highest_score);
		highestScore.setText("" + brewStats.getHighestScore());

		TextView lowestScore = (TextView) rootView.findViewById(R.id.lowest_score);
		lowestScore.setText("" + brewStats.getLowestScore());

		TextView avgPlayerScore = (TextView) rootView.findViewById(R.id.avg_player_score);
		avgPlayerScore.setText("" + brewStats.getAverageScore());

		TextView avgPlayersPerRound = (TextView) rootView.findViewById(R.id.avg_players_per_round);
		avgPlayersPerRound.setText("" + brewStats.getAverageNumberOfPlayers());

		final ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.player_stats_expandable_list);

		// adapter = new BrewGroupsExpandableListAdapter(brewGroups, getActivity());
		// listView.setAdapter(adapter);

		return rootView;
	}

}