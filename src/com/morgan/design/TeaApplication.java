package com.morgan.design;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;

@ReportsCrashes(formKey = "dHlyYXloTmQ4X0lObDhEeHRnY2Eyd2c6MQ")
public class TeaApplication extends Application {

	private final Logger LOG = LoggerFactory.getLogger(TeaApplication.class);

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
		// The following line triggers the initialisation of ACRA
		ACRA.init(this);
		super.onCreate();
		LOG.debug("APPLICATION onCreate");
	}

	@Override
	public void onTerminate() {
		LOG.debug("APPLICATION onTerminate");
		super.onTerminate();
	}

}
