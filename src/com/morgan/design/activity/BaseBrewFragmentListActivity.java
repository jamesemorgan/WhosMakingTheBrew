package com.morgan.design.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.morgan.design.db.BrewRepository;
import com.morgan.design.db.DatabaseHelper;

public class BaseBrewFragmentListActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

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

	public void shortToast(final CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
