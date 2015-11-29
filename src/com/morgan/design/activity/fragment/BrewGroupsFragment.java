package com.morgan.design.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.activity.TeaRoundHomeActivity;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter;
import com.morgan.design.adaptor.refactor.BrewGroupsExpandableListAdapter.OnRemovePlayerFromGroup;
import com.morgan.design.db.domain.BrewGroup;
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.utils.InputUtils;

import java.util.List;

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
        loadGroups();
    }

    @Override
    public void onTrashClicked(BrewPlayer player, int groupPosition, int childPosition) {

        BrewGroup group = brewGroups.get(groupPosition);

        group.getBrewPlayers().remove(player);

        getBrewRepository().updateGroup(group);

        getBrewRepository().deletePlayer(player);

        getBrewRepository().removePlayerStats(player);

        if (group.hasNoPlayers()) {
            getBrewRepository().deleteGroup(group);
        }
        loadGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_brew_groups, container, false);

        final ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.brew_groups_expandable_list_view);

        adapter = new BrewGroupsExpandableListAdapter(brewGroups, getActivity(), this);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemType = ExpandableListView.getPackedPositionType(id);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    // int childPosition = ExpandableListView.getPackedPositionChild(id);
                    // int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    return false; // true if we consumed the click, false if not
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    launchManagegroupDialog(ExpandableListView.getPackedPositionGroup(id));
                    return true; // true if we consumed the click, false if not

                }
                // null item; we don't consume the click
                return false;
            }
        });

        return rootView;
    }

    protected void launchManagegroupDialog(int groupPosition) {

        final BrewGroup group = brewGroups.get(groupPosition);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setCancelable(true);
        builder.setTitle("Manage Group: " + group.getName());
        builder.setMessage(" >> Load Brew Group\n" + " >> Rename Group\n" + " >> Delete Group");
        builder.setNegativeButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int id) {
                loadBrewGroup(group);
            }
        }).setNeutralButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int id) {
                renameBrewGroup(group);
            }
        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int id) {
                deleteBrewGroup(group);
            }
        }).create().show();

    }

    protected void deleteBrewGroup(BrewGroup group) {
        for (final BrewPlayer player : group.getBrewPlayers()) {
            getBrewRepository().removePlayerStats(player);
        }
        getBrewRepository().deleteGroup(group);
        loadGroups();
    }

    protected void renameBrewGroup(final BrewGroup group) {

        final LayoutInflater factory = LayoutInflater.from(getActivity());

        final View textEntryView = factory.inflate(R.layout.add_group_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Edit Group Name").setIcon(R.drawable.add_group)
                .setView(textEntryView).create();

        final EditText addGroupEditText = (EditText) textEntryView.findViewById(R.id.add_group_edit_text);
        addGroupEditText.setText(group.getName());
        addGroupEditText.setFocusable(true);
        addGroupEditText.requestFocus();

        InputUtils.showKeyBoard(addGroupEditText, getActivity());

        dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton) {
                final String groupName = addGroupEditText.getEditableText().toString();
                InputUtils.hideKeyBoard(addGroupEditText, getActivity());
                addGroupEditText.clearFocus();
                group.setName(groupName);
                getBrewRepository().updateGroup(group);
                shortToast("Updated Group: " + group.getName());
                loadGroups();
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton) {
                InputUtils.hideKeyBoard(addGroupEditText, getActivity());
                addGroupEditText.clearFocus();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    protected void loadBrewGroup(BrewGroup group) {
        final Intent intent = new Intent(getActivity(), TeaRoundHomeActivity.class);
        intent.putExtra(TeaApplication.GROUP_ID, group.getId());
        startActivity(intent);
    }

    private void loadGroups() {
        brewGroups = getBrewRepository().findAllBrewGroups();
        adapter.setBrewGroups(brewGroups);
    }
}
