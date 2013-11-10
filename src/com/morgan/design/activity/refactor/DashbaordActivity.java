package com.morgan.design.activity.refactor;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.activity.BaseBrewFragmentActivity;
import com.morgan.design.fragment.refactor.BrewGroupsFragment;
import com.morgan.design.fragment.refactor.BrewHomePageFragments;
import com.morgan.design.fragment.refactor.BrewStatsFragments;
import com.morgan.design.helpers.Changelog;
import com.morgan.design.utils.Prefs;
import com.morgan.design.utils.Utils;

public class DashbaordActivity extends BaseBrewFragmentActivity implements ActionBar.TabListener {

	private static Logger LOG = LoggerFactory.getLogger(DashbaordActivity.class);

	public static final int DASHBOARD_GROUPS = 0;
	public static final int DASHBOARD_HOME = 1;
	public static final int DASHBOARD_STATS = 2;

	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashbaord);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding tab. We can also use ActionBar.Tab#select()
		// to do this if we have a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by the adapter. Also specify this Activity
			// object, which implements the TabListener interface, as the callback (listener) for when this tab is
			// selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		Intent intent = getIntent();
		int selectedView = intent.getIntExtra(TeaApplication.EXTRA_DASHBOARD_VIEW, DASHBOARD_HOME);

		actionBar.setSelectedNavigationItem(selectedView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dashboard_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.db_credits:
				Utils.openCredits(this);
				return true;
			case R.id.db_feedback:
				Utils.openFeedback(this);
				return true;
			case R.id.db_settings:
				Prefs.openPreferenecesActivity(this);
				return true;
			case R.id.db_changelog:
				Changelog.show(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
				case DASHBOARD_GROUPS:
					fragment = new BrewGroupsFragment();
					break;
				case DASHBOARD_HOME:
					fragment = new BrewHomePageFragments();
					break;
				case DASHBOARD_STATS:
					fragment = new BrewStatsFragments();
					break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3; // Show 3 total pages.
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case DASHBOARD_GROUPS:
					return getString(R.string.dashboard_groups).toUpperCase(l);
				case DASHBOARD_HOME:
					return getString(R.string.dashboard_brew_round).toUpperCase(l);
				case DASHBOARD_STATS:
					return getString(R.string.dashboard_stats).toUpperCase(l);
			}
			return null;
		}
	}

}
