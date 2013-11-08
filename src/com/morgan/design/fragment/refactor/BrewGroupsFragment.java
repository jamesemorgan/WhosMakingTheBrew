package com.morgan.design.fragment.refactor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.morgan.design.R;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter;
import com.morgan.design.db.domain.BrewGroup;

public class BrewGroupsFragment extends Fragment implements OnLongClickListener {

	private static Logger LOG = LoggerFactory.getLogger(BrewGroupsFragment.class);

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
