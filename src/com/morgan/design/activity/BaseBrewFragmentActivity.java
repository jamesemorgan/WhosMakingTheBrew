package com.morgan.design.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.morgan.design.db.BrewRepository;
import com.morgan.design.db.DatabaseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseBrewFragmentActivity extends OrmLiteBaseFragment<DatabaseHelper> {

    public final Logger log = LoggerFactory.getLogger(getClass());

    private BrewRepository mBrewRepository;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
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
