package com.morgan.design;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.PreferencesUtils;

/**
 * @author James Edward Morgan
 */
public class TeaRoundPreferences extends PreferenceActivity {

	private static final String LOG_TAG = "TeaRoundPreferences";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen, final Preference pref) {
		Logger.d(LOG_TAG, "Finding preferences : " + pref.getKey());

		if (pref.getKey().equals(PreferencesUtils.PREF_CHANGELOG)) {
			findPreference(PreferencesUtils.PREF_CHANGELOG).setOnPreferenceChangeListener(
					new Preference.OnPreferenceChangeListener() {
						@Override
						public boolean onPreferenceChange(final Preference arg0, final Object clicked) {
							final Boolean booleanClicked = (Boolean) clicked;
							return PreferencesUtils.setChangelogPref(getApplicationContext(), booleanClicked);
						}
					});
		}

		if (pref.getKey().equals(PreferencesUtils.PREF_TIMER_VIBRATE)) {
			findPreference(PreferencesUtils.PREF_TIMER_VIBRATE).setOnPreferenceChangeListener(
					new Preference.OnPreferenceChangeListener() {
						@Override
						public boolean onPreferenceChange(final Preference arg0, final Object clicked) {
							final Boolean booleanClicked = (Boolean) clicked;
							return PreferencesUtils.setTimerVibrationPref(getApplicationContext(), booleanClicked);
						}
					});
		}

		if (pref.getKey().equals(PreferencesUtils.PREF_GOOGLE_ANALYTIC)) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(TeaRoundPreferences.this);
			builder.setMessage(
					"Google Analytics will anonymously track and report a user's activity"
							+ " inside of this application. Click Ok if you state you understand what this "
							+ "means and information will be sent to Google and the developer of this application.")
					.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int id) {
							PreferencesUtils.setGoogleAnalyticsPref(getApplicationContext(), true);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int id) {
							PreferencesUtils.setGoogleAnalyticsPref(getApplicationContext(), false);
						}
					}).create();
			builder.show();

			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, pref);
	}

}
