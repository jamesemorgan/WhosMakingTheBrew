package com.morgan.design.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Build;

public class BuildUtils {

	private final static Logger LOG = LoggerFactory.getLogger(BuildUtils.class);

	public static boolean isNotRunningEmmulator() {
		return isRunningEmmulator();
	}

	public static String getDeviceId() {
		String AndroidID = System.getProperty(android.provider.Settings.Secure.ANDROID_ID);
		if (AndroidID == null) {
			AndroidID = "a23456790112345b";
		}
		final String Android_ID = Build.ID + "-" + android.os.Build.PRODUCT + "-" + AndroidID;
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
		}
		catch (final Exception e) {
			LOG.error("Error gettting version code", e);
		}
		return versionCode;
	}

	public static boolean isRunningEmmulator() {
		return "sdk".equals(Build.PRODUCT);
	}
}
