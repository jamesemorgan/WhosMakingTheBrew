package com.morgan.design.activity;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.morgan.design.db.BrewRepository;
import com.morgan.design.db.DatabaseHelper;

public class BaseBrewListActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	private BrewRepository mBrewRepository;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBrewRepository = new BrewRepository(getHelper());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public final BrewRepository getBrewRepository() {
		return mBrewRepository;
	}
}
