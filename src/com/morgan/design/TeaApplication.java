package com.morgan.design;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.morgan.design.helpers.Logger;

@ReportsCrashes(formKey = "dHlyYXloTmQ4X0lObDhEeHRnY2Eyd2c6MQ")
public class TeaApplication extends Application {

	public static final String LOG_TAG = "BaseApplication";

	public static final int ACTIVITY_HOME = 1;
	public static final int ACTIVITY_RUNNING = 2;
	public static final int ACTIVITY_RESULTS = 3;
	public static final int ACTIVITY_PREFERENCES = 4;
	public static final int ACTIVITY_GROUP = 5;
	public static final int ACTIVITY_CREDITS = 6;

	public static final String PLAYER_IDS = "player_ids";
	public static final String GROUP_ID = "group_id";

	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
		Logger.d(LOG_TAG, "APPLICATION onCreate");
	}

	@Override
	public void onTerminate() {
		Logger.d(LOG_TAG, "APPLICATION onTerminate");
		super.onTerminate();
	}

}
