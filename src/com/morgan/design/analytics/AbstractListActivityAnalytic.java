package com.morgan.design.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.morgan.design.db.BrewRepository;
import com.morgan.design.db.DatabaseHelper;
import com.morgan.design.utils.GoogleAnalyticsUtils;
import com.morgan.design.utils.PreferencesUtils;

public class AbstractListActivityAnalytic extends OrmLiteBaseListActivity<DatabaseHelper> implements GoogleAnalyticsActivity {

	private final Logger LOG = LoggerFactory.getLogger(AbstractListActivityAnalytic.class);

	private GoogleAnalyticsTracker tracker;
	private static BrewRepository brewRepository;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		brewRepository = new BrewRepository(getHelper());
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTracker(GoogleAnalyticsTracker.getInstance());
		GoogleAnalyticsUtils.start(tracker, this);
	}

	private void setTracker(final GoogleAnalyticsTracker tracker) {
		this.tracker = tracker;
	}

	@Override
	public void trackPageView(final String pageView) {
		if (PreferencesUtils.getGoogleAnalyticsPref(this)) {
			tracker.trackPageView(pageView);
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void trackEvent(final String category, final String action, final String label, final int value) {
		if (PreferencesUtils.getGoogleAnalyticsPref(this)) {
			LOG.debug("Google Analytics is enabled | TrackEvent=[{}, {}, {}, {}]", category, action, label, value);
			tracker.trackEvent(category, action, label, value);
		}
	}

	public final BrewRepository getBrewRepository() {
		return brewRepository;
	}
}
