package com.morgan.design.activity.fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.morgan.design.activity.OrmLiteFragment;
import com.morgan.design.db.BrewRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseBrewFragment extends OrmLiteFragment {

    public final Logger log = LoggerFactory.getLogger(getClass());

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