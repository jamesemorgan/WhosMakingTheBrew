package com.morgan.design.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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
import com.morgan.design.analytics.AbstractActivityAnalytic;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.db.domain.TeaRound;
import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.Utils;

public class TeaRoundGeneratorResultsActivity extends AbstractActivityAnalytic {

	private final static String LOG_TAG = "Results";

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
		this.playersList = getBrewRepository().getPlayersByIds(playerIds);
		if (this.playersList.isEmpty()) {
			Utils.shortToast(this, "Unable to determine winner, no players found");
		}
		else {
			this.resultsList = TeaRound.determineWinner(this.playersList);

			// Set adaptor
			this.resultAdaptor = new ResultAdaptor(this, R.layout.player_results_row, this.resultsList);

			// Set animation effect
			final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,
					R.anim.results_lists_layout_controller);
			this.resultList.setLayoutAnimation(controller);
			this.resultList.setAdapter(this.resultAdaptor);

			final BrewPlayer winner = this.resultsList.get(0);
			this.winnerText.setText(winner.getName());
			this.winnerScore.setText(Integer.toString(winner.getScore()));

			getBrewRepository().deleteAllLastRunEntries();
			getBrewRepository().insertLastRunEntry(this.resultsList);
			getBrewRepository().saveBrewStats(this.resultsList);
			getBrewRepository().savePlayerStats(this.resultsList);
		}
	}

	private void findAllViewsById() {
		this.winnerText = (TextView) findViewById(R.id.winner_text);
		this.winnerScore = (TextView) findViewById(R.id.winner_score);
		this.resultList = (ListView) findViewById(R.id.results_list);
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
			Logger.d(LOG_TAG, "back button pressed");
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
			case R.id.email_results:
				final Spanned emailBody = Html.fromHtml(Utils.generateResultsEmail(this.resultsList));
				Utils.createEmailIntenet(this, "text/html", "Sending results...", "Whos Making The Brew?",
						new String[] {}, emailBody);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void playAgain() {
		final Intent returnIntent = new Intent();
		// returnIntent.putExtra(TeaApplication.PLAYER_REF, new
		// ArrayList<BrewPlayer>(this.playersList));
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}

}
