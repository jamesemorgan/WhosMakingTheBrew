package com.morgan.design.helpers;

import android.app.Activity;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdViewer {

	public static void displayBanner(final Activity activity, final int addHolderRes) {
		// Create the adView
		final AdView adView = new AdView(activity, AdSize.BANNER, Constants.ADMOB_PUBLISHER_ID);
		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		final LinearLayout layout = (LinearLayout) activity.findViewById(addHolderRes);
		// Add the adView to it
		layout.addView(adView);

		// Initiate a generic request to load it with an ad
		adView.loadAd(createAdRequest(activity));
	}

	/**
	 * Assumes LinearLayout layout with ID of R.id.add_holder
	 * 
	 * @param activity
	 *            the {@link Activity}
	 */
	public static void displayBanner(final Activity activity) {
		// displayBanner(activity, R.id.add_holder);
	}

	public static AdView createBanner(final Activity activity) {
		final AdView adView = new AdView(activity, AdSize.BANNER, Constants.ADMOB_PUBLISHER_ID);
		adView.loadAd(createAdRequest(activity));
		return adView;
	}

	private static AdRequest createAdRequest(final Activity activity) {
		final AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice("E6DDB050A08DD7BEF32DA80946EC3EED");
		return adRequest;
	}

}
