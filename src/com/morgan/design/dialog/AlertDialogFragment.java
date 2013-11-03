package com.morgan.design.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.morgan.design.R;

public class AlertDialogFragment extends DialogFragment {

	public static AlertDialogFragment newInstance(boolean editing, String groupName) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("editing", editing);
		args.putString("groupName", groupName);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//@formatter:off
		int title = getArguments().getInt("title");
//		new AlertDialog.Builder(getActivity()).setIcon(R.drawable.alert_dialog_icon).setTitle(title)
//				.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//						((FragmentAlertDialog) getActivity()).doPositiveClick();
//					}
//				}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//						((FragmentAlertDialog) getActivity()).doNegativeClick();
//					}
//				}).create();
		//@formatter:on

		final boolean editing = getArguments().getBoolean("editing");
		String groupName = getArguments().getString("groupName");

		final LayoutInflater factory = LayoutInflater.from(getActivity());
		final View textEntryView = factory.inflate(R.layout.add_group_dialog, null);
		final AlertDialog dialogGroup = new AlertDialog.Builder(getActivity()).setTitle(editing ? "Edit Group Name" : "Add Group Name")
				.setIcon(R.drawable.add_group).setView(textEntryView).create();

		final EditText addGroupEditText = (EditText) textEntryView.findViewById(R.id.add_group_edit_text);

		if (editing) {
			addGroupEditText.setText(groupName);
		}

		addGroupEditText.setFocusable(true);
		addGroupEditText.requestFocus();

		// ((TeaRoundGeneratorHomeActivity) getActivity()).showKeyBoard(addGroupEditText);
		//
		// dialogGroup.setButton(editing ? getString(R.string.edit) : getString(R.string.add), new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(final DialogInterface dialog, final int whichButton) {
		// final String groupName = addGroupEditText.getEditableText().toString();
		//
		// ((TeaRoundGeneratorHomeActivity) getActivity()).hideKeyBoard(addGroupEditText);
		//
		// addGroupEditText.clearFocus();
		// if (editing) {
		// ((TeaRoundGeneratorHomeActivity) getActivity()).updateGroupName(groupName);
		// }
		// else {
		// ((TeaRoundGeneratorHomeActivity) getActivity()).addPlayersToGroup(groupName);
		// }
		// }
		// });
		// dialogGroup.setButton2(getString(R.string.cancel), new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(final DialogInterface dialog, final int whichButton) {
		// ((TeaRoundGeneratorHomeActivity) getActivity()).hideKeyBoard(addGroupEditText);
		// addGroupEditText.clearFocus();
		// dialog.cancel();
		// }
		// });
		return dialogGroup;
	}

}
