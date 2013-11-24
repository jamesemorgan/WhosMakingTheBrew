package com.morgan.design.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.helpers.Constants;
import com.morgan.design.utils.BuildUtils;

public class FeedbackFormFragmentActivity extends FragmentActivity {

	private EditText nameField;
	private EditText emailField;
	private EditText feedbackField;
	private Spinner feedbackSpinner;
	private CheckBox responseCheckbox;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_form);

		nameField = (EditText) findViewById(R.id.EditTextName);
		emailField = (EditText) findViewById(R.id.EditTextEmail);
		feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
		feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
		responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
	}

	public void onSendFeedback(final View button) {

		final String name = nameField.getText().toString();
		final String email = emailField.getText().toString();
		final String feedback = feedbackField.getText().toString();
		final String feedbackType = feedbackSpinner.getSelectedItem().toString();
		final boolean bRequiresResponse = responseCheckbox.isChecked();

		// Take the fields and format the message contents
		final String subject = formatFeedbackSubject(feedbackType);

		final String message = formatFeedbackMessage(feedbackType, name, email, feedback, bRequiresResponse, BuildUtils.getVersion(this));

		// Create the message
		sendFeedbackMessage(subject, message);
	}

	protected String formatFeedbackSubject(final String feedbackType) {
		final String strFeedbackSubjectFormat = getResources().getString(R.string.feedbackmessagesubject_format);
		return String.format(strFeedbackSubjectFormat, feedbackType);
	}

	protected String formatFeedbackMessage(final String feedbackType, final String name, final String email, final String feedback,
			final boolean bRequiresResponse, final String version) {
		return String.format("Type: %s \n\n %s \n\n %s (%s) - %s \n\n Version: %s \n\n Android Version: %s", feedbackType, feedback, name,
				email, getResponseString(bRequiresResponse), version, Build.VERSION.RELEASE);

	}

	protected String getResponseString(final boolean bRequiresResponse) {
		if (bRequiresResponse == true) {
			return getResources().getString(R.string.feedbackmessagebody_responseyes);
		}
		return getResources().getString(R.string.feedbackmessagebody_responseno);
	}

	public void sendFeedbackMessage(final String subject, final String message) {

		final Intent messageIntent = new Intent(android.content.Intent.ACTION_SEND);
		messageIntent.putExtra(android.content.Intent.EXTRA_EMAIL, Constants.APP_FEEDBACK_EMAIL);
		messageIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		messageIntent.setType("plain/text");
		messageIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

		try {
			startActivity(messageIntent);
		}
		catch (final android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
}
