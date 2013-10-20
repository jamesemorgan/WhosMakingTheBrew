package com.morgan.design.activity.general;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.helpers.Constants;
import com.morgan.design.utils.BuildUtils;

public class FormActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_form);

	}

	public void onSendFeedback(final View button) {

		final EditText nameField = (EditText) findViewById(R.id.EditTextName);
		final String name = nameField.getText().toString();

		final EditText emailField = (EditText) findViewById(R.id.EditTextEmail);
		final String email = emailField.getText().toString();

		final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
		final String feedback = feedbackField.getText().toString();

		final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
		final String feedbackType = feedbackSpinner.getSelectedItem().toString();

		final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
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
