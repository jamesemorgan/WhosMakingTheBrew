package com.morgan.design.fragment.refactor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.adaptor.refactor.BrewPlayerStatsExpandableListAdapter;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;

public class BrewStatsFragments extends BaseBrewFragment {

	private static Logger LOG = LoggerFactory.getLogger(BrewStatsFragments.class);

	private BrewStats brewStats;
	private List<PlayerStats> playerStats;

	private TextView totalRoundsRun;
	private TextView highestScore;
	private TextView lowestScore;
	private TextView avgPlayerScore;
	private TextView avgPlayersPerRound;

	private BrewPlayerStatsExpandableListAdapter adapter;
	private ExpandableListView playerStatsExpandableListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playerStats = getBrewRepository().getPlayerStats();
		brewStats = getBrewRepository().getBrewStats();
	}

	@Override
	public void onResume() {
		super.onResume();
		playerStats = getBrewRepository().getPlayerStats();
		brewStats = getBrewRepository().getBrewStats();

		adapter.setPlayerStats(playerStats);

		populateGameStats();
	}

	private void populateGameStats() {
		totalRoundsRun.setText("" + brewStats.getTotalTimesRun());
		highestScore.setText("" + brewStats.getHighestScore());
		lowestScore.setText("" + brewStats.getLowestScore());
		avgPlayerScore.setText("" + brewStats.getAverageScore());
		avgPlayersPerRound.setText("" + brewStats.getAverageNumberOfPlayers());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_stats, container, false);

		totalRoundsRun = (TextView) rootView.findViewById(R.id.total_rounds_run);
		highestScore = (TextView) rootView.findViewById(R.id.highest_score);
		lowestScore = (TextView) rootView.findViewById(R.id.lowest_score);
		avgPlayerScore = (TextView) rootView.findViewById(R.id.avg_player_score);
		avgPlayersPerRound = (TextView) rootView.findViewById(R.id.avg_players_per_round);

		playerStatsExpandableListView = (ExpandableListView) rootView.findViewById(R.id.player_stats_expandable_list);

		adapter = new BrewPlayerStatsExpandableListAdapter(playerStatsExpandableListView, playerStats, getActivity());
		playerStatsExpandableListView.setAdapter(adapter);

		return rootView;
	}

}