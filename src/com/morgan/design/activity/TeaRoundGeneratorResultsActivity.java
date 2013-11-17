package com.morgan.design.activity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.adaptor.ResultAdaptor;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.TeaRound;

public class TeaRoundGeneratorResultsActivity extends BaseBrewActivity {

	private final Logger LOG = LoggerFactory.getLogger(TeaRoundGeneratorResultsActivity.class);

	private ResultAdaptor resultAdaptor;

	private TextView winnerText;
	private TextView winnerScore;
	private ListView resultList;

	private List<BrewPlayer> playersList;
	private List<BrewPlayer> resultsList;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.results);

		findAllViewsById();
		final ArrayList<Integer> playerIds = getIntent().getExtras().getIntegerArrayList(TeaApplication.PLAYER_IDS);
		playersList = getBrewRepository().getPlayersByIds(playerIds);
		if (playersList.isEmpty()) {
			shortToast("Unable to determine winner, no players found");
		}
		else {
			resultsList = TeaRound.determineWinner(playersList);

			// Set adaptor
			resultAdaptor = new ResultAdaptor(this, R.layout.player_results_row, resultsList);

			// Set animation effect
			final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.results_lists_layout_controller);
			resultList.setLayoutAnimation(controller);
			resultList.setAdapter(resultAdaptor);

			final BrewPlayer winner = resultsList.get(0);
			winnerText.setText(winner.getName());
			winnerScore.setText(Integer.toString(winner.getScore()));

			saveAndCleanUpDB();
		}
	}

	private void saveAndCleanUpDB() {
		getBrewRepository().deleteAllLastRunEntries();
		getBrewRepository().insertLastRunEntry(resultsList);
		getBrewRepository().saveBrewStats(resultsList);
		getBrewRepository().savePlayerStats(resultsList);
	}

	private void findAllViewsById() {
		winnerText = (TextView) findViewById(R.id.winner_text);
		winnerScore = (TextView) findViewById(R.id.winner_score);
		resultList = (ListView) findViewById(R.id.results_list);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.results_menu, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			LOG.debug("back button pressed");
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.play_again:
				playAgain();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void playAgain() {
		final Intent returnIntent = new Intent();
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}

}
