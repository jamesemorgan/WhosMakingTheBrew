package com.morgan.design.activity;

import static com.morgan.design.utils.ObjectUtils.isNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.activity.refactor.DashbaordActivity;
import com.morgan.design.adaptor.PlayerAdaptor;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.utils.BuildUtils;
import com.morgan.design.utils.Prefs;
import com.morgan.design.utils.StringUtils;
import com.morgan.design.utils.Utils;

public class TeaRoundGeneratorHomeActivity extends BaseBrewFragmentActivity {

	private final Logger LOG = LoggerFactory.getLogger(TeaRoundGeneratorHomeActivity.class);

	private AutoCompleteTextView addPlayerEditText;
	private Button runTeaRoundButton;
	private TextView playerDetailHeader;
	private ListView playersListAdapter;

	@Deprecated
	private static final int DIALOG_ADD_GROUP = 0;
	@Deprecated
	private static final int DIALOG_EDIT_GROUP = 1;

	private PlayerAdaptor playerAdaptor;
	private BrewGroup brewGroup;
	private Collection<BrewPlayer> brewPlayers;

	private DataSetObserver dataSetObserver;

	@Override
	public void onCreate(final Bundle savedInstanceState) {

		BuildUtils.logBuildDetails();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tea_round_home);

		findAllViewsById();

		// Bind focus listener to attach to add player text box
		addPlayerEditText.setOnKeyListener(new EnterPlayerClickListener());

		// Load the Contact Name from List into the AutoCompleteTextView
		addPlayerEditText.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.single_contact_view,
				R.id.single_contact_view_name, getAllContactNames()));

		playerAdaptor = new PlayerAdaptor(this, new ArrayList<BrewPlayer>(), new TrashClickHandler());

		dataSetObserver = new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				enableDisableButtonRunTeaRound();
				setPlayerDetailHeader();
			}
		};
		playerAdaptor.registerDataSetObserver(dataSetObserver);
		playersListAdapter.setAdapter(playerAdaptor);

		final Bundle bundle = getIntent().getExtras();
		if (isNotNull(bundle)) {
			final Integer groupId = bundle.getInt(TeaApplication.GROUP_ID);
			if (null != groupId) {
				loadGroup(groupId);
			}
		}
		else {
			loadLastRunPlayers();
		}
	}

	private List<String> getAllContactNames() {
		Set<String> lContactNamesList = new HashSet<String>();
		try {
			String[] projection = new String[] { Data._ID, ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.PHOTO_THUMBNAIL_URI };

			// Get all Contacts
			Cursor lPeople = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
			if (lPeople != null) {
				while (lPeople.moveToNext()) {
					// Add Contact's Name into the List
					int index = lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String value = lPeople.getString(index);
					LOG.debug("Lookup index {} : value {}", index, value);
					if (value != null) {
						lContactNamesList.add(value);
					}
				}
			}
		}
		catch (NullPointerException e) {
			LOG.error("Unable to get contacts", e.getMessage());
		}
		List<String> values = new ArrayList<String>();
		values.addAll(lContactNamesList);
		return values;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		playerAdaptor.unregisterDataSetObserver(dataSetObserver);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		menu.clear();
		final MenuInflater inflater = getMenuInflater();
		if (null != brewGroup) {
			inflater.inflate(R.menu.brew_round_menu_edit_group, menu);
		}
		else {
			inflater.inflate(R.menu.brew_round_menu_add_group, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.br_manage_groups:
				final Intent intent = new Intent(this, DashbaordActivity.class);
				intent.putExtra(TeaApplication.EXTRA_DASHBOARD_VIEW, DashbaordActivity.DASHBOARD_GROUPS);
				startActivityForResult(intent, TeaApplication.ACTIVITY_GROUP);
				return true;
			case R.id.br_credits:
				Utils.openCredits(this);
				return true;
			case R.id.br_feedback:
				Utils.openFeedback(this);
				return true;
			case R.id.br_settings:
				Prefs.openPreferenecesActivity(this);
				return true;
			case R.id.br_add_group:
				if (validNumberOfPlayers()) {
					showDialog(DIALOG_ADD_GROUP);
				}
				else {
					shortToast("Not enough players, minimum 2!");
				}
				return true;
			case R.id.br_edit_group:
				if (validNumberOfPlayers()) {
					showDialog(DIALOG_EDIT_GROUP);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch (id) {
			case DIALOG_ADD_GROUP:
				dialog = showGroupDialog(false);
				break;
			case DIALOG_EDIT_GROUP:
				dialog = showGroupDialog(true);
				break;
		}
		return dialog;
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		// See which child activity is calling us back.
		switch (requestCode) {
			case TeaApplication.ACTIVITY_HOME: {
				if (resultCode == RESULT_OK) {
					//
				}
				break;
			}
			case TeaApplication.ACTIVITY_RUNNING: {
				if (resultCode == RESULT_OK) {
					final Intent resultsIntent = new Intent(this, TeaRoundGeneratorResultsActivity.class);
					final ArrayList<Integer> playerIds = new ArrayList<Integer>();
					for (final BrewPlayer player : brewPlayers) {
						playerIds.add(player.getId());
					}
					final Bundle bundle = new Bundle();
					bundle.putIntegerArrayList(TeaApplication.PLAYER_IDS, playerIds);
					resultsIntent.putExtras(bundle);
					startActivityForResult(resultsIntent, TeaApplication.ACTIVITY_RESULTS);
				}
				break;
			}
			case TeaApplication.ACTIVITY_RESULTS: {
				if (resultCode == RESULT_OK) {
					loadLastRunPlayers();
				}
				break;
			}
			case TeaApplication.ACTIVITY_GROUP: {
				if (resultCode == RESULT_OK) {
					final Integer groupId = data.getIntExtra(TeaApplication.GROUP_ID, 0);
					if (0 != groupId && null != groupId) {
						loadGroup(groupId);
					}

				}
				break;
			}
			case TeaApplication.ACTIVITY_PREFERENCES: {
				break;
			}
			default:
				break;
		}
	}

	public void enableDisableButtonRunTeaRound() {
		runTeaRoundButton.setEnabled(validNumberOfPlayers());
	}

	private void clearBrewBroupIfRequired() {
		if (playerAdaptor.isEmpty() && null != brewGroup) {
			brewGroup = null;
		}
	}

	public void onAddPlayer(final View view) {
		addPlayer();
	}

	public void onRunTeaRound(final View view) {
		runTeaRound();
	}

	public void setPlayerDetailHeader() {
		final StringBuilder builder = new StringBuilder();

		if (playerAdaptor.isEmpty()) {
			builder.append(getResources().getString(R.string.no_players));
		}
		else {
			builder.append("Players(" + playerAdaptor.getCount() + ")");
			if (null != brewGroup && StringUtils.isNotBlank(brewGroup.getName())) {
				builder.append(" | Group: ").append(brewGroup.getName());
			}
		}
		playerDetailHeader.setText(builder.toString());
	}

	private void addPlayer() {
		final String playerName = addPlayerEditText.getText().toString();

		if (notNullEmpyOrValue(playerName, R.string.add_player)) {
			shortToast("Invalid player name");
			return;
		}

		final BrewPlayer player = new BrewPlayer();
		player.setName(playerName);

		if (!checkAlreadyPlaying(player)) {
			LOG.debug("Added player, name: {}", player.getName());

			brewPlayers.add(player);
			playerAdaptor.add(player);

			addPlayerEditText.setText("");
			addPlayerEditText.clearComposingText();
			addPlayerEditText.requestFocus();

			// TODO set brew group if present
			getBrewRepository().saveBrewPlayer(player);
		}
		else {
			shortToast(player.getName() + " already playing");
			LOG.debug("{} already playing", player.getName());
		}
		enableDisableButtonRunTeaRound();
		setPlayerDetailHeader();
	}

	public void addPlayersToGroup(final String groupName) {
		if (notNullEmpyOrValue(groupName, R.string.group_management)) {
			shortToast("Invalid Group Name");
			return;
		}
		brewGroup = getBrewRepository().saveGroup(groupName, brewPlayers);
		shortToast("Create Group: " + groupName);
		setPlayerDetailHeader();
	}

	private boolean checkAlreadyPlaying(final BrewPlayer player) {
		if (null == brewPlayers) {
			return false;
		}
		for (final BrewPlayer existingPlayer : brewPlayers) {
			if (existingPlayer.getName().equals(player.getName())) {
				return true;
			}
		}
		return false;
	}

	private void findAllViewsById() {
		playerDetailHeader = (TextView) findViewById(R.id.player_detail_header);
		addPlayerEditText = (AutoCompleteTextView) findViewById(R.id.add_player_text);
		runTeaRoundButton = (Button) findViewById(R.id.run_tea_round);
		playersListAdapter = (ListView) findViewById(R.id.players_list_adator);
	}

	public void hideKeyBoard(final View view) {
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private void loadGroup(final Integer groupId) {
		brewGroup = getBrewRepository().findGroupById(groupId);
		if (null != brewGroup) {
			brewPlayers = brewGroup.getBrewPlayers();
			playerAdaptor.clear();
			for (final BrewPlayer player : brewGroup.getBrewPlayers()) {
				playerAdaptor.add(player);
			}
		}
		setPlayerDetailHeader();
		enableDisableButtonRunTeaRound();
	}

	private void loadLastRunPlayers() {
		brewPlayers = getBrewRepository().findLastRunPlayers();

		if (null != brewPlayers && !brewPlayers.isEmpty() && null != brewPlayers.iterator().next().getBrewGroup()) {
			brewGroup = brewPlayers.iterator().next().getBrewGroup();
		}

		if (brewPlayers != null && !brewPlayers.isEmpty()) {
			playerAdaptor.clear();
			for (final BrewPlayer player : brewPlayers) {
				playerAdaptor.add(player);
			}
		}
		// Set no players
		enableDisableButtonRunTeaRound();
		setPlayerDetailHeader();
	}

	private boolean notNullEmpyOrValue(final String value, final int resource) {
		return value == null || value.equals("") || getResources().getString(resource).equals(value);
	}

	private void runTeaRound() {
		final Intent splash = new Intent(this, TeaRoundGeneratorRunningActivity.class);
		startActivityForResult(splash, TeaApplication.ACTIVITY_RUNNING);
	}

	private AlertDialog showGroupDialog(final boolean editing) {
		final LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.add_group_dialog, null);
		final AlertDialog dialogGroup = new AlertDialog.Builder(this).setTitle(editing ? "Edit Group Name" : "Add Group Name")
				.setIcon(R.drawable.add_group).setView(textEntryView).create();

		final EditText addGroupEditText = (EditText) textEntryView.findViewById(R.id.add_group_edit_text);

		if (editing) {
			addGroupEditText.setText(brewGroup.getName());
		}

		addGroupEditText.setFocusable(true);
		addGroupEditText.requestFocus();

		showKeyBoard(addGroupEditText);
		dialogGroup.setButton(editing ? getString(R.string.edit) : getString(R.string.add), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int whichButton) {
				final String groupName = addGroupEditText.getEditableText().toString();
				hideKeyBoard(addGroupEditText);
				addGroupEditText.clearFocus();
				if (editing) {
					updateGroupName(groupName);
				}
				else {
					addPlayersToGroup(groupName);
				}
			}
		});
		dialogGroup.setButton2(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int whichButton) {
				hideKeyBoard(addGroupEditText);
				addGroupEditText.clearFocus();
				dialog.cancel();
			}
		});
		return dialogGroup;
	}

	public void showKeyBoard(final View view) {
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public void updateGroupName(final String groupName) {
		brewGroup.setName(groupName);
		getBrewRepository().updateGroup(brewGroup);
	}

	private boolean validNumberOfPlayers() {
		return playerAdaptor.getCount() >= 2;
	}

	public class TrashClickHandler implements OnClickListener {

		@Override
		public void onClick(final View v) {
			final Integer position = (Integer) v.getTag();
			LOG.debug("Trash clicked, position {}", position);
			final BrewPlayer player = playerAdaptor.getItem(position);

			if (0 != player.getId()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(TeaRoundGeneratorHomeActivity.this);
				builder.setMessage("Click 'Remove' to remove from this game, 'Delete' to delete completely").setCancelable(true)
						.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, final int id) {
								removeFromGame(player, position);
							}
						}).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, final int id) {
								deletePlayer(player, position);
							}

						}).create().show();
			}
			else {
				removeFromGame(player, position);
			}
		}
	}

	private void deletePlayer(final BrewPlayer player, final int playerPosition) {
		shortToast("Deleted " + player.getName() + " from game");
		playerAdaptor.remove(player);
		getBrewRepository().deletePlayer(player);
		getBrewRepository().removePlayerStats(player);
		clearBrewBroupIfRequired();
	}

	private void removeFromGame(final BrewPlayer player, final int playerPosition) {
		shortToast("Removed " + player.getName() + " from game");
		playerAdaptor.remove(player);
		clearBrewBroupIfRequired();
	}

	@Override
	public void shortToast(final CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private class EnterPlayerClickListener implements OnKeyListener {
		@Override
		public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
				addPlayer();
				addPlayerEditText.setFocusableInTouchMode(true);
				addPlayerEditText.requestFocusFromTouch();
				addPlayerEditText.setSelected(true);
				return true;
			}
			return false;
		}
	}

}
