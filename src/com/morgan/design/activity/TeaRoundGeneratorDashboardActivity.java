package com.morgan.design.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.analytics.AbstractActivityAnalytic;
import com.morgan.design.helpers.AdViewer;
import com.morgan.design.helpers.Changelog;
import com.morgan.design.helpers.Constants;
import com.morgan.design.utils.PreferencesUtils;
import com.morgan.design.utils.Utils;

public class TeaRoundGeneratorDashboardActivity extends AbstractActivityAnalytic {

	// http://www.androiduipatterns.com/2011/02/ui-design-pattern-dashboard.html

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		AdViewer.displayBanner(this);
		Changelog.show(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check opening location
		final Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			final boolean openedFromWidget = bundle.getBoolean(Constants.TEA_ROUND_WIDGET);
			if (openedFromWidget) {
				trackEvent("Clicks", "Button", "OpenedFromWidget", 1);
			}
		}
	}

	public void onBrewGroupsClick(final View v) {
		final Intent intent = new Intent(this, TeaRoundGroupManagementActivity.class);
		trackPageView("/TeaRoundGroupManagementActivity");
		startActivityForResult(intent, TeaApplication.ACTIVITY_GROUP);
	}

	public void onBrewRoundClick(final View v) {
		final Intent home = new Intent(this, TeaRoundGeneratorHomeActivity.class);
		trackPageView("/TeaRoundGeneratorHomeActivity");
		startActivity(home);
	}

	public void onBrewStatsClick(final View v) {
		final Intent home = new Intent(this, TeaRoundGeneratorStatisticsActivity.class);
		trackPageView("/TeaRoundGeneratorStatisticsActivity");
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
