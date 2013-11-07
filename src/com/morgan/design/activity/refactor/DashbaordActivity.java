package com.morgan.design.activity.refactor;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.activity.BaseBrewFragmentActivity;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewStats;
import com.morgan.design.db.domain.PlayerStats;

public class DashbaordActivity extends BaseBrewFragmentActivity implements ActionBar.TabListener {

	private static Logger LOG = LoggerFactory.getLogger(DashbaordActivity.class);

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. 
	 * We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded 
	 * fragment in memory. If this becomes too memory intensive, it may be best to switch to a 
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/** The {@link ViewPager} that will host the section contents. */
	private ViewPager mViewPager;

	private List<BrewGroup> groups;
	private BrewStats brewStats;
	private List<PlayerStats> playerStats;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashbaord);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		groups = getBrewRepository().findAllBrewGroups();
		brewStats = getBrewRepository().getBrewStats();
		playerStats = getBrewRepository().getPlayerStats();

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

		// Set selected tab to brew round
		actionBar.setSelectedNavigationItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashbaord, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;

			switch (position) {
				case 0:
					fragment = new BrewGroupsFragment();
					((BrewGroupsFragment) fragment).setBrewGroups(groups);
					break;
				case 2:
					fragment = new BrewStatsFragments();
					((BrewStatsFragments) fragment).setBrewStats(brewStats);
					((BrewStatsFragments) fragment).setPlayerStats(playerStats);
					break;
				default:
					fragment = new DummySectionFragment();
					Bundle args = new Bundle();
					args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
					break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.dashboard_groups).toUpperCase(l);
				case 1:
					return getString(R.string.dashboard_brew_round).toUpperCase(l);
				case 2:
					return getString(R.string.dashboard_stats).toUpperCase(l);
			}
			return null;
		}
	}

	public static class BrewStatsFragments extends Fragment {

		private BrewStats brewStats;
		private List<PlayerStats> playerStats;

		public void setBrewStats(BrewStats brewStats) {
			this.brewStats = brewStats;
		}

		public void setPlayerStats(List<PlayerStats> playerStats) {
			this.playerStats = playerStats;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_stats, container, false);

			TextView totalRoundsRun = (TextView) rootView.findViewById(R.id.total_rounds_run);
			totalRoundsRun.setText("" + brewStats.getTotalTimesRun());

			TextView highestScore = (TextView) rootView.findViewById(R.id.highest_score);
			highestScore.setText("" + brewStats.getHighestScore());

			TextView lowestScore = (TextView) rootView.findViewById(R.id.lowest_score);
			lowestScore.setText("" + brewStats.getLowestScore());

			TextView avgPlayerScore = (TextView) rootView.findViewById(R.id.avg_player_score);
			avgPlayerScore.setText("" + brewStats.getAverageScore());

			TextView avgPlayersPerRound = (TextView) rootView.findViewById(R.id.avg_players_per_round);
			avgPlayersPerRound.setText("" + brewStats.getAverageNumberOfPlayers());

			return rootView;
		}

	}

	public static class BrewGroupsFragment extends Fragment implements OnLongClickListener {

		private ExpandableListAdapter adapter;
		private List<BrewGroup> brewGroups;

		public void setBrewGroups(List<BrewGroup> brewGroups) {
			this.brewGroups = brewGroups;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_groups, container, false);
			final ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.brew_groups_expandable_list_view);

			adapter = new BrewGroupsExpandableListAdapter(brewGroups, getActivity());

			listView.setAdapter(adapter);

			// TODO check this works?
			// TODO on long click prompt for rename, deletion actions
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					long packedPosition = listView.getExpandableListPosition(position);
					if (ExpandableListView.getPackedPositionType(packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
						// get item ID's
						int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
						int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

						// handle data
						LOG.debug("Long Click Group Pos {} Child Pos {}", groupPosition, childPosition);

						// return true as we are handling the event.
						return true;
					}
					return false;
				}
			});
			return rootView;
		}

		@Override
		public boolean onLongClick(View view) {
			return false;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dashbaord_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
