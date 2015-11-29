package com.morgan.design.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.provider.Settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildUtils {

    private final static Logger LOG = LoggerFactory.getLogger(BuildUtils.class);

    public static boolean isNotRunningEmmulator() {
        return isRunningEmmulator();
    }

    public static void logBuildDetails() {
        LOG.debug("##########################################");
        LOG.debug("Codename: {}", Build.VERSION.CODENAME);
        LOG.debug("SDK Version: {}", Integer.toString(Build.VERSION.SDK_INT));
        LOG.debug("Release Version: {}", Build.VERSION.RELEASE);
        LOG.debug("Device: {}", Build.DEVICE);
        LOG.debug("Manufacturer: {}", Build.MANUFACTURER);
        LOG.debug("##########################################");
    }

    public static String getDeviceId() {
        String AndroidID = System.getProperty(Settings.Secure.ANDROID_ID);
        if (AndroidID == null) {
            AndroidID = Build.UNKNOWN;
        }
        final String Android_ID = Build.ID + "-" + Build.PRODUCT + "-" + AndroidID;
        LOG.debug("#########################################");
        LOG.debug("Android_ID = {}", Android_ID);
        LOG.debug("#########################################");
        return Android_ID;
    }

    public static String getVersion(final Activity activity) {
        String versionCode = "- | -";
        try {
            final PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            versionCode = new StringBuilder().append("").append(Integer.toString(pi.versionCode)).append(" | ").append(pi.versionName)
                    .toString();
        } catch (final Exception e) {
            LOG.error("Error gettting version code", e);
        }
        return versionCode;
    }

    public static boolean isRunningEmmulator() {
        return "sdk".equals(Build.PRODUCT);
    }
}
