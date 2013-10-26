package com.morgan.design.activity.preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.morgan.design.R;
import com.morgan.design.utils.Prefs;

public class PreferencesFragment extends PreferenceFragment {

	private final Logger LOG = LoggerFactory.getLogger(PreferencesFragment.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen, final Preference pref) {
		LOG.debug("Finding preferences: {}", pref.getKey());

		if (pref.getKey().equals(Prefs.PREF_CHANGELOG)) {
			findPreference(Prefs.PREF_CHANGELOG).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(final Preference pref, final Object clicked) {
					final Boolean booleanClicked = (Boolean) clicked;
					return Prefs.setChangelogPref(getActivity(), booleanClicked);
				}
			});
		}
		if (pref.getKey().equals(Prefs.PREF_GOOGLE_ANALYTIC)) {
			findPreference(Prefs.PREF_CHANGELOG).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(final Preference pref, final Object clicked) {
					final Boolean booleanClicked = (Boolean) clicked;
					return Prefs.setGoogleAnalyticsPref(getActivity(), booleanClicked);
				}
			});
		}
		return super.onPreferenceTreeClick(preferenceScreen, pref);
	}

}