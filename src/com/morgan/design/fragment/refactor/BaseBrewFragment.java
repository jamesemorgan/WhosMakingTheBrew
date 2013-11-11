package com.morgan.design.fragment.refactor;

import android.os.Bundle;
import android.widget.Toast;

import com.morgan.design.db.BrewRepository;

public class BaseBrewFragment extends OrmLiteFragment {

	private BrewRepository mBrewRepository;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBrewRepository = new BrewRepository(getHelper());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public final BrewRepository getBrewRepository() {
		return mBrewRepository;
	}

	public void shortToast(final CharSequence message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}