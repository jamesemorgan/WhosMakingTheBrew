package com.morgan.design.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.adaptor.GroupAdaptor;
import com.morgan.design.analytics.AbstractListActivityAnalytic;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.Utils;

public class TeaRoundGroupManagementActivity extends AbstractListActivityAnalytic {

	private static final String LOG_TAG = "Group";

	private static final int MENU_LOAD = Menu.FIRST + 1;
	private static final int MENU_DELETE = Menu.FIRST + 2;

	private List<BrewGroup> groups = new ArrayList<BrewGroup>();
	private GroupAdaptor groupAdaptor;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_management);

		findAllViewsById();
		loadGroups();
	}

	private void findAllViewsById() {
		//
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			final String groupName = this.groups.get(info.position).getName();
			menu.setHeaderTitle(groupName);
			menu.add(Menu.NONE, MENU_LOAD, MENU_LOAD, "Load Group");
			menu.add(Menu.NONE, MENU_DELETE, MENU_DELETE, "Delete Group");
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final int menuItemIndex = item.getItemId();
		final BrewGroup group = this.groups.get(info.position);

		if (menuItemIndex == MENU_LOAD) {
			Logger.d(LOG_TAG, "Loading players");
			final Intent returnIntent = new Intent();
			returnIntent.putExtra(TeaApplication.GROUP_ID, group.getId());
			setResult(RESULT_OK, returnIntent);
			finish();
		}
		else if (menuItemIndex == MENU_DELETE) {
			Utils.shortToast(this, "Removed group: " + group.getName());
			removeGroup(group);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Logger.d(LOG_TAG, "back button pressed");
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void loadGroups() {
		this.groups.clear();
		this.groups = getBrewRepository().findAllBrewGroups();
		this.groupAdaptor = new GroupAdaptor(this, R.layout.group_data_row, this.groups);
		setListAdapter(this.groupAdaptor);
		registerForContextMenu(getListView());
		this.groupAdaptor.notifyDataSetChanged();
	}

	public void removeGroup(final BrewGroup group) {
		for (final BrewPlayer player : group.getBrewPlayers()) {
			getBrewRepository().removePlayerStats(player);
		}
		getBrewRepository().deleteGroup(group);
		loadGroups();
	}
}
