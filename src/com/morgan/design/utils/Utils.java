package com.morgan.design.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.activity.TeaRoundGeneratorCreditsActivity;
import com.morgan.design.activity.general.FormActivity;
import com.morgan.design.analytics.GoogleAnalyticsActivity;
import com.morgan.design.helpers.Constants;

public class Utils {

	private final static Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static void createEmailIntenet(final Activity activity, final String title, final String type, final String subject,
			final String[] toAddresses, final Spanned body) {

		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType(type);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, toAddresses);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		emailIntent.putExtra(Intent.EXTRA_TEXT, body);
		try {
			activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
		catch (final android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	public static View dialogWebView(final Context context, final String fileName) {
		final View view = View.inflate(context, R.layout.dialog_webview, null);
		final WebView web = (WebView) view.findViewById(R.id.wv_dialog);
		web.loadUrl("file:///android_asset/" + fileName);
		return view;
	}

	public static String getSuffix(final int n) {
		if (n >= 11 && n <= 13) {
			return "th";
		}
		switch (n % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}

	public static final Vibrator getVibrator(final Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public static void openCredits(final GoogleAnalyticsActivity activity) {
		try {
			final Intent intent = new Intent(activity.getActivity(), TeaRoundGeneratorCreditsActivity.class);
			activity.getActivity().startActivityForResult(intent, TeaApplication.ACTIVITY_CREDITS);
			activity.trackPageView("/TeaRoundGeneratorCreditsActivity");
		}
		catch (final Exception e) {
			Utils.shortToast(activity.getActivity(), "Unable to open credits");
		}
	}

	public static void openFeedback(final Activity activity) {
		try {
			activity.startActivity(new Intent(activity, FormActivity.class));
		}
		catch (final Exception e) {
			Utils.shortToast(activity, "Unable to open feedback");
		}
	}

	public static void openUrl(final Activity activity, final String url) {
		try {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}
		catch (final Exception e) {
			Utils.shortToast(activity, "Unable to open URL");
		}
	}

	public static void openDonate(final GoogleAnalyticsActivity activity) {
		try {
			activity.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DONATE_URL)));
			activity.trackPageView("/OpenDonatePage|" + activity.getClass().getCanonicalName());
		}
		catch (final Exception e) {
			Utils.shortToast(activity.getActivity(), "Unable to open URL");
		}
	}

	public static void openWeb(final GoogleAnalyticsActivity activity) {
		try {
			activity.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MORGAN_DESIGN)));
			activity.trackPageView("/OpenMorganDesignPage|" + activity.getClass().getCanonicalName());
		}
		catch (final Exception e) {
			Utils.shortToast(activity.getActivity(), "Unable to open URL");
		}
	}

	private static void sendEmail(final Activity activity, final String title, final String subject, final String[] toAddresses) {
		createEmailIntenet(activity, title, "message/rfc822", subject, toAddresses);
	}

	public static void shortToast(final Activity activity, final CharSequence message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}

	private static void createEmailIntenet(final Activity activity, final String title, final String type, final String subject,
			final String[] toAddresses) {
		createEmailIntenet(activity, title, type, subject, toAddresses, null);
	}

	public static void openBug(final GoogleAnalyticsActivity activity) {
		Utils.sendEmail(activity.getActivity(), "Send mail...", "Who's Making The Brew : Bug Found", Constants.MY_EMAIL);
		activity.trackPageView("/OpenBugEmail|" + activity.getClass().getCanonicalName());
	}

	public static void openImprovement(final GoogleAnalyticsActivity activity) {
		Utils.sendEmail(activity.getActivity(), "Send mail...", "Who's Making The Brew : Improvement", Constants.MY_EMAIL);
		activity.trackPageView("/OpenImprovmentEmail|" + activity.getClass().getCanonicalName());
	}

	public static void logBuildDetails() {
		LOG.debug("##########################################");
		LOG.debug(Build.VERSION.CODENAME);
		LOG.debug(Build.VERSION.RELEASE);
		LOG.debug(Build.VERSION.SDK);
		LOG.debug("##########################################");
	}

}
