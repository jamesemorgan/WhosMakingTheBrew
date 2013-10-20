package com.morgan.design.utils;

import android.app.Activity;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.morgan.design.helpers.Constants;

public class GoogleAnalyticsUtils {

	public static final void start(final GoogleAnalyticsTracker tracker, final Activity activity) {
		if (PreferencesUtils.getGoogleAnalyticsPref(activity)) {
			// Start the tracker in manual dispatch mode...
			tracker.start(Constants.GOOGLE_ANALYTIC_ACCOUNT_ID, 20, activity);
		}
		else {
			stopTracker(tracker);
		}
	}

	public static final void stopTracker(final GoogleAnalyticsTracker tracker) {
		try {
			tracker.stop();
		}
		catch (final Exception e) {
			// Nothing happened NPE thrown as not started in first place
		}
	}

}
