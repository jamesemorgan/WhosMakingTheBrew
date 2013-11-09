package com.morgan.design.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.morgan.design.db.BrewRepository;
import com.morgan.design.db.DatabaseHelper;

@Deprecated
public class BaseBrewActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private BrewRepository mBrewRepository;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBrewRepository = new BrewRepository(getHelper());
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public final BrewRepository getBrewRepository() {
		return mBrewRepository;
	}

	public void shortToast(final CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
