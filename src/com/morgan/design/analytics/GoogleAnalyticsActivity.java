package com.morgan.design.analytics;

import android.app.Activity;

public interface GoogleAnalyticsActivity {

	Activity getActivity();

	/**
	 * @param pageView
	 */
	void trackPageView(String pageView);

	/**
	 * @param Category -> "Clicks"
	 * @param Action -> "Button"
	 * @param label -> "clicked"
	 * @param value -> 77
	 */
	void trackEvent(String category, String action, String label, int value);

}
