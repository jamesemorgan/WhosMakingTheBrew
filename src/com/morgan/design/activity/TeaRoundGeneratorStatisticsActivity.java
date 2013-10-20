package com.morgan.design.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.morgan.design.R;
import com.morgan.design.analytics.AbstractActivityAnalytic;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.helpers.Logger;
import com.morgan.design.ui.SeparatedListAdapter;

/**
 * @author James Edward Morgan
 */
public class TeaRoundGeneratorStatisticsActivity extends AbstractActivityAnalytic {

	private final static String LOG_TAG = "BrewStats";

	private final static String ITEM_TITLE = "title";
	private final static String ITEM_CAPTION = "caption";

	private static final String[] FROM = new String[] { ITEM_TITLE, ITEM_CAPTION };
	private static final int[] TO = new int[] { R.id.list_complex_title, R.id.list_complex_caption };

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		// create our list and custom adapter
		final SeparatedListAdapter adapter = new SeparatedListAdapter(this, R.layout.list_header);
		final ListView list = (ListView) findViewById(R.id.stats_list);

		final BrewStats brewStats = getBrewRepository().getBrewStats();

		final List<Map<String, ?>> stats = new LinkedList<Map<String, ?>>();
		stats.add(createItem("Total Times Run", Integer.toString(brewStats.getTotalTimesRun())));
		stats.add(createItem("Highest Score", Integer.toString(brewStats.getHighestScore())));
		stats.add(createItem("Lowest Score", Integer.toString(brewStats.getLowestScore())));
		stats.add(createItem("Avg. Player Score", Integer.toString(brewStats.getAverageScore())));
		stats.add(createItem("Avg. Players Per Brew", Integer.toString(brewStats.getAverageNumberOfPlayers())));

		adapter.addSection("Stats", new SimpleAdapter(this, stats, R.layout.list_complex, FROM, TO));

		list.setAdapter(adapter);
	}

	public void onPlayerStatsClick(final View view) {
		Logger.d(LOG_TAG, "Player stats clicked");
		final Intent playerStats = new Intent(this, TeaRoundPlayerStatisticsActivity.class);
		trackPageView("/TeaRoundPlayerStatisticsActivity");
		startActivity(playerStats);
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
				getBrewRepository().clearAllBrewStats();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private Map<String, ?> createItem(final String title, final String caption) {
		final Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

}
