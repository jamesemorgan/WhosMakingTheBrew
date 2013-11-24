package com.morgan.design.dialog.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundGeneratorHomeActivity;

public class ManageGroupDialogFragment extends DialogFragment {

	public static final String EDIT_MODE = "edit_mode";
	public static final String BREW_GROUP_NAME = "brew_group_name";
	public static final String TAG = "add_group_dialog_fragment";

	public interface ManageGroupDialogFragmentActions {
		void updateBrewGroup(String groupName);

		void createBrewGroup(String groupName);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final LayoutInflater factory = LayoutInflater.from(getActivity());

		final Boolean editing = getArguments().getBoolean(EDIT_MODE);

		final View textEntryView = factory.inflate(R.layout.add_group_dialog, null);

		final AlertDialog dialogGroup = new AlertDialog.Builder(getActivity()).setTitle(editing ? "Edit Group Name" : "Add Group Name")
				.setIcon(R.drawable.add_group).setView(textEntryView).create();

		final EditText addGroupEditText = (EditText) textEntryView.findViewById(R.id.add_group_edit_text);

		if (editing) {
			String brewGroupName = getArguments().getString(BREW_GROUP_NAME);
			addGroupEditText.setText(brewGroupName);
		}

		addGroupEditText.setFocusable(true);
		addGroupEditText.requestFocus();

		showKeyBoard(addGroupEditText);

		dialogGroup.setButton(Dialog.BUTTON_POSITIVE, editing ? getString(R.string.edit) : getString(R.string.add),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int whichButton) {
						final String groupName = addGroupEditText.getEditableText().toString();
						hideKeyBoard(addGroupEditText);
						addGroupEditText.clearFocus();

						if (editing) {
							((TeaRoundGeneratorHomeActivity) getActivity()).updateBrewGroup(groupName);
						}
						else {
							((TeaRoundGeneratorHomeActivity) getActivity()).createBrewGroup(groupName);
						}
					}
				});
		dialogGroup.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int whichButton) {
				hideKeyBoard(addGroupEditText);
				addGroupEditText.clearFocus();
				dialog.cancel();
			}
		});
		return dialogGroup;
	}

	public void hideKeyBoard(final View view) {
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void showKeyBoard(final View view) {
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

}
