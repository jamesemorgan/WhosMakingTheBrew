package com.morgan.design.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.morgan.design.R;
import com.morgan.design.utils.BuildUtils;
import com.morgan.design.utils.Prefs;
import com.morgan.design.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Changelog {

    private final static Logger LOG = LoggerFactory.getLogger(Changelog.class);

    public static boolean show(final Activity activity) {

        final int prefVersion = Prefs.getAppVersionPref(activity);
        int currentVersion = 0;

        final boolean overrideChangeLog = BuildUtils.isRunningEmulator();
        LOG.debug("Overriding ChangeLog: {}", overrideChangeLog);

        final boolean showChangeLog = Prefs.getChangelogPref(activity);

        try {
            final PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            currentVersion = pi.versionCode;
        } catch (final NameNotFoundException e) {
            LOG.error("Package name not found", e);
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
        Prefs.setAppVersionPref(activity, currentVersion);
        return true;
    }

    protected static void showChangelogDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.changelog_title)
                .setView(Utils.dialogWebView(activity, activity.getString(R.string.changelog_filename)))
                .setPositiveButton(R.string.ok, null).setNegativeButton(R.string.feedback, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                Utils.openFeedback(activity);
            }
        }).show();
    }
}
