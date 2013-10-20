package com.morgan.design.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.morgan.design.TeaApplication;
import com.morgan.design.TeaRoundPreferences;
import com.morgan.design.db.domain.TimeFields;

public class PreferencesUtils {

	public static final String PREF_CHANGELOG = "changelog";
	public static final String PREF_TIMER_VIBRATE = "timerVibration";
	public static final String PREF_GOOGLE_ANALYTIC = "googleAnalytics";
	public static final String PREF_APP_VERSION = "app.version";

	public static final String HOUR_KEY = "SharedPreference_HourKey";
	public static final String MINUTE_KEY = "SharedPreference_MinuteKey";
	public static final String SECOND_KEY = "SharedPreference_SecondKey";

	private static SharedPreferences getPrefs(final Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static boolean getChangelogPref(final Context context) {
		return getPrefs(context).getBoolean(PREF_CHANGELOG, true);
	}

	public static boolean setChangelogPref(final Context context, final boolean value) {
		return getPrefs(context).edit().putBoolean(PREF_CHANGELOG, value).commit();
	}

	public static boolean getTimerVibrationPref(final Context context) {
		return getPrefs(context).getBoolean(PREF_TIMER_VIBRATE, true);
	}

	public static boolean setTimerVibrationPref(final Context context, final boolean value) {
		return getPrefs(context).edit().putBoolean(PREF_TIMER_VIBRATE, value).commit();
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

	public static TimeFields getTimerFields(final Context context) {
		final TimeFields fields = new TimeFields();
		try {
			fields.setHour(getPrefs(context).getInt(HOUR_KEY, 00));
			fields.setMinute(getPrefs(context).getInt(MINUTE_KEY, 00));
			fields.setSecond(getPrefs(context).getInt(SECOND_KEY, 00));
		}
		catch (final Exception e) {
			//
		}
		return fields;
	}

	@Deprecated
	public static void setTimerFields(final Context context, final TimeFields timeFields) {
		getPrefs(context).edit().putInt(HOUR_KEY, timeFields.getHour()).commit();
		getPrefs(context).edit().putInt(MINUTE_KEY, timeFields.getMinute()).commit();
		getPrefs(context).edit().putInt(SECOND_KEY, timeFields.getSecond()).commit();
	}

	public static void openPreferenecesActivity(final Activity activity) {
		final Intent intent = new Intent(activity, TeaRoundPreferences.class);
		activity.startActivityForResult(intent, TeaApplication.ACTIVITY_PREFERENCES);
	}

	// public static SharedPreferences.Editor getPrivateSharedPrefsEditor(final
	// Context context) {
	// final SharedPreferences preferences = getPrivateSharedPrefs(context);
	// final SharedPreferences.Editor editor = preferences.edit();
	// return editor;
	// }
	//
	// public static SharedPreferences getPrivateSharedPrefs(final Context
	// context) {
	// return context.getSharedPreferences(Constants.TEA_ROUND_SHARED_PREFS_KEY,
	// Activity.MODE_PRIVATE);
	// }
	//
	// public static Preference findChangeLogPref(final PreferenceActivity
	// preferenceActivity) {
	// return preferenceActivity.findPreference(PREF_CHANGELOG);
	// }
	//
	// public static boolean getVibratePref(final Context context) {
	// final SharedPreferences preferences =
	// PreferencesUtils.getPrivateSharedPrefs(context);;
	// return preferences.getBoolean(PREF_TIMER_VIBRATE, true);
	// }
	//
	// public static Preference findTimerVibrationPref(final PreferenceActivity
	// preferenceActivity) {
	// return preferenceActivity.findPreference(PREF_TIMER_VIBRATE);
	// }
	//
	// public static void openPreferenecesActivity(final Activity activity) {
	// final Intent intent = new Intent(activity, TeaRoundPreferences.class);
	// activity.startActivityForResult(intent,
	// TeaApplication.ACTIVITY_PREFERENCES);
	// }
	//
	// public static Preference findGoogleAnalyticsPref(final PreferenceActivity
	// preferenceActivity) {
	// return preferenceActivity.findPreference(PREF_GOOGLE_ANALYTIC);
	// }
}
