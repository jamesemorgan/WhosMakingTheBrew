package com.morgan.design.activity.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.morgan.design.R;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter.OnRemovePlayerFromGroup;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;

public class BrewGroupsFragment extends BaseBrewFragment implements OnRemovePlayerFromGroup {

	private BrewGroupsExpandableListAdapter adapter;
	private List<BrewGroup> brewGroups;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		brewGroups = getBrewRepository().findAllBrewGroups();
	}

	@Override
	public void onResume() {
		super.onResume();
		brewGroups = getBrewRepository().findAllBrewGroups();
		adapter.setBrewGroups(brewGroups);
	}

	@Override
	public void onTrashClicked(BrewPlayer player, int groupPosition, int childPosition) {

		BrewGroup group = brewGroups.get(groupPosition);

		group.getBrewPlayers().remove(player);

		getBrewRepository().updateGroup(group);

		getBrewRepository().deletePlayer(player);

		if (group.hasNoPlayers()) {
			getBrewRepository().deleteGroup(group);
		}

		brewGroups = getBrewRepository().findAllBrewGroups();
		adapter.setBrewGroups(brewGroups);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_groups, container, false);

		final ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.brew_groups_expandable_list_view);

		adapter = new BrewGroupsExpandableListAdapter(brewGroups, getActivity(), this);

		listView.setAdapter(adapter);

		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				adapter.getGroup(groupPosition);
				return false;
			}
		});

		return rootView;
	}
}
