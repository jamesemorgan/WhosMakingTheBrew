package com.morgan.design;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.morgan.design.utils.BuildUtils;

@ReportsCrashes(formKey = "dHlyYXloTmQ4X0lObDhEeHRnY2Eyd2c6MQ")
public class TeaApplication extends Application {

	// List of Activity result flags
	public static final int ACTIVITY_HOME = 1;
	public static final int ACTIVITY_RUNNING = 2;
	public static final int ACTIVITY_RESULTS = 3;
	public static final int ACTIVITY_PREFERENCES = 4;
	public static final int ACTIVITY_GROUP = 5;
	public static final int ACTIVITY_CREDITS = 6;

	// List of keys used in intent extras
	public static final String EXTRA_DASHBOARD_VIEW = "EXTRA_DASHBOARD_VIEW";

	// List of domain constants
	public static final String PLAYER_IDS = "player_ids";
	public static final String GROUP_ID = "group_id";

	@Override
	public void onCreate() {
		ACRA.init(this);

		BuildUtils.logBuildDetails();

		super.onCreate();
	}
}
