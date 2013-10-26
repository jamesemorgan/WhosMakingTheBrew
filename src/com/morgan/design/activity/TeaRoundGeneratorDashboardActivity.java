package com.morgan.design.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.helpers.Changelog;
import com.morgan.design.utils.PreferencesUtils;
import com.morgan.design.utils.Utils;

public class TeaRoundGeneratorDashboardActivity extends BaseBrewActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		Changelog.show(this);
	}

	public void onBrewGroupsClick(final View v) {
		final Intent intent = new Intent(this, TeaRoundGroupManagementActivity.class);
		startActivityForResult(intent, TeaApplication.ACTIVITY_GROUP);
	}

	public void onBrewRoundClick(final View v) {
		final Intent home = new Intent(this, TeaRoundGeneratorHomeActivity.class);
		startActivity(home);
	}

	public void onBrewStatsClick(final View v) {
		final Intent home = new Intent(this, TeaRoundGeneratorStatisticsActivity.class);
		startActivity(home);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.db_credits:
				Utils.openCredits(this);
				return true;
			case R.id.db_feedback:
				Utils.openFeedback(this);
				return true;
			case R.id.db_settings:
				PreferencesUtils.openPreferenecesActivity(this);
				return true;
			case R.id.db_changelog:
				Changelog.show(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		// See which child activity is calling us back.
		switch (requestCode) {
			case TeaApplication.ACTIVITY_GROUP: {
				if (resultCode == RESULT_OK) {
					final Intent home = new Intent(this, TeaRoundGeneratorHomeActivity.class);
					home.putExtra(TeaApplication.GROUP_ID, data.getIntExtra(TeaApplication.GROUP_ID, 0));
					startActivity(home);
				}
				break;
			}
			default:
				break;
		}
	}

}
