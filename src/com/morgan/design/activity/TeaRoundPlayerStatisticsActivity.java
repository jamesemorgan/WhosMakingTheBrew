package com.morgan.design.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.morgan.design.R;
import com.morgan.design.analytics.AbstractActivityAnalytic;
import com.morgan.design.db.domain.PlayerStats;
import com.morgan.design.helpers.AdViewer;
import com.morgan.design.ui.SeparatedListAdapter;

/**
 * @author James Edward Morgan
 */
public class TeaRoundPlayerStatisticsActivity extends AbstractActivityAnalytic {

	private final static String PLAYER_NAME = "player_name";
	private final static String NUM_ENTERED = "num_games_entered";
	private final static String NUM_WON = "num_games_won";
	private final static String HIGHEST_SCORE = "highest_score";
	private final static String LOWEST_SCORE = "lowest_score";

	private static final int[] TO = new int[] { R.id.player_name, R.id.num_games_entered, R.id.num_games_won,
			R.id.highest_score, R.id.lowest_score };
	private static final String[] FROM = new String[] { PLAYER_NAME, NUM_ENTERED, NUM_WON, HIGHEST_SCORE, LOWEST_SCORE };

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_stats);
		AdViewer.displayBanner(this);

		final SeparatedListAdapter adapter = new SeparatedListAdapter(this, R.layout.list_header);
		final ListView list = (ListView) findViewById(R.id.stats_list);

		final List<PlayerStats> playerStats = getBrewRepository().getPlayerStats();

		final List<Map<String, ?>> stats = new LinkedList<Map<String, ?>>();

		for (final PlayerStats playerStat : playerStats) {
			if (null == playerStat.getBrewPlayer()) {
				// Clean up broken stats form pre 1.25 release
				getBrewRepository().removePlayerStats(playerStat);
			}
			else {
				stats.add(createItem(playerStat));
			}
		}

		adapter.addSection("Player Statistics",
				new SimpleAdapter(this, stats, R.layout.list_player_stats_row, FROM, TO));

		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stats_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.stats_clear_stats:
				getBrewRepository().clearAllPlayerStats();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private Map<String, ?> createItem(final PlayerStats playerStats) {
		final Map<String, String> item = new HashMap<String, String>();
		item.put(PLAYER_NAME, playerStats.getBrewPlayer().getName());
		item.put(NUM_ENTERED, "# Brews Entered: " + Integer.toString(playerStats.getTotalTimesRun()));
		item.put(NUM_WON, "# Brews Won: " + Integer.toString(playerStats.getTotalTimesWon()));
		item.put(HIGHEST_SCORE, "Highest Score: " + Integer.toString(playerStats.getHighestScore()));
		item.put(LOWEST_SCORE, "Lowest Score: " + Integer.toString(playerStats.getLowestScore()));
		return item;
	}
}
