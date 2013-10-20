package com.morgan.design.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.morgan.design.R;
import com.morgan.design.utils.BuildUtils;
import com.morgan.design.utils.PreferencesUtils;
import com.morgan.design.utils.Utils;

public class Changelog {

	private static final String LOG_TAG = "Changelog";

	public static boolean show(final Activity activity) {

		final int prefVersion = PreferencesUtils.getAppVersionPref(activity);
		int currentVersion = 0;

		final boolean overrideChangeLog = BuildUtils.isRunningEmmulator();
		Logger.d(LOG_TAG, "Overriding ChangeLog: " + overrideChangeLog);

		final boolean showChangeLog = PreferencesUtils.getChangelogPref(activity);

		try {
			final PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			currentVersion = pi.versionCode;
		}
		catch (final NameNotFoundException e) {
			Logger.e(LOG_TAG, "Package name not found", e);
			return false;
		}

		// Not added before
		if (prefVersion == 0 && showChangeLog) {
			showChangelogDialog(activity);
		}
		// On upgrade
		else if (overrideChangeLog || (showChangeLog && currentVersion > prefVersion)) {
			showChangelogDialog(activity);
		}
		PreferencesUtils.setAppVersionPref(activity, currentVersion);
		return true;
	}

	protected static void showChangelogDialog(final Activity activity) {
		new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.changelog_title)
				.setView(Utils.dialogWebView(activity, activity.getString(R.string.changelog_filename)))
				.setPositiveButton(R.string.ok, null)
				.setNegativeButton(R.string.feedback, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						Utils.openFeedback(activity);
					}
				}).show();
	}
}
