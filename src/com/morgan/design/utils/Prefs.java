package com.morgan.design.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.morgan.design.TeaApplication;
import com.morgan.design.activity.preferences.SettingsActivity;

public class Prefs {

	public static final String PREF_CHANGELOG = "changelog";
	public static final String PREF_GOOGLE_ANALYTIC = "googleAnalytics";
	public static final String PREF_APP_VERSION = "app.version";

	private static SharedPreferences getPrefs(final Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static boolean getChangelogPref(final Context context) {
		return getPrefs(context).getBoolean(PREF_CHANGELOG, true);
	}

	public static boolean setChangelogPref(final Context context, final boolean value) {
		return getPrefs(context).edit().putBoolean(PREF_CHANGELOG, value).commit();
	}

	public static boolean getGoogleAnalyticsPref(final Context context) {
		return getPrefs(context).getBoolean(PREF_GOOGLE_ANALYTIC, true);
	}

	public static boolean setGoogleAnalyticsPref(final Context context, final boolean value) {
		return getPrefs(context).edit().putBoolean(PREF_GOOGLE_ANALYTIC, value).commit();
	}

	public static int getAppVersionPref(final Context context) {
		return getPrefs(context).getInt(PREF_APP_VERSION, 0);
	}

	public static boolean setAppVersionPref(final Context context, final int value) {
		return getPrefs(context).edit().putInt(PREF_APP_VERSION, value).commit();
	}

	public static void openPreferenecesActivity(final Activity activity) {
		final Intent intent = new Intent(activity, SettingsActivity.class);
		activity.startActivityForResult(intent, TeaApplication.ACTIVITY_PREFERENCES);
	}

}
