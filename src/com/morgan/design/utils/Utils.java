package com.morgan.design.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.helpers.Constants;

public class Utils {
	private static final String LINEBREAK = "<br />";

	public static void createEmailIntenet(final Activity activity, final String title, final String type,
			final String subject, final String[] toAddresses, final Spanned body) {

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

	public static String generateResultsEmail(final List<BrewPlayer> winners) {
		// @formatter:off
		final EmailBuilder endContent = new EmailBuilder().content()
				.h3(winners.get(0).getName() + " Won The Tea Round......!!!")
				.p("Scores as it stands: ").p("").table()
				.row(" #   |   Score   |   Name" + LINEBREAK)
				.row("---------------------------------------" + LINEBREAK)
				.row(generateResultsRow(winners))
				.row("---------------------------------------" + LINEBREAK)
				.endTable()
				.p("<a href='http://www.morgan-design.com'>Morgan-Design</a>")
				.endContent();
		// @formatter:on

		return endContent.build();
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

	private static void sendEmail(final Activity activity, final String title, final String subject,
			final String[] toAddresses) {
		createEmailIntenet(activity, title, "message/rfc822", subject, toAddresses);
	}

	public static void shortToast(final Activity activity, final CharSequence message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}

	private static void createEmailIntenet(final Activity activity, final String title, final String type,
			final String subject, final String[] toAddresses) {
		createEmailIntenet(activity, title, type, subject, toAddresses, null);
	}

	private static List<String> generateResultsRow(final List<BrewPlayer> winners) {
		final List<String> players = new ArrayList<String>();

		for (int i = 0; i < winners.size(); i++) {
			// @formatter:off
			players.add(result(i) + " | " + score(winners, i) + " | "
					+ name(winners, i) + LINEBREAK);
			// @formatter:on
		}
		return players;
	}

	private static String name(final List<BrewPlayer> winners, final int index) {
		return winners.get(index).getName();

	}

	private static String result(final int index) {
		final int position = index + 1;
		return StringUtils.rightPad(position + Utils.getSuffix(position), 4);
	}

	private static String score(final List<BrewPlayer> winners, final int index) {
		return StringUtils.rightPad(Integer.toString(winners.get(index).getScore()), 5);
	}

	public static void openBug(final GoogleAnalyticsActivity activity) {
		Utils.sendEmail(activity.getActivity(), "Send mail...", "Who's Making The Brew : Bug Found", Constants.MY_EMAIL);
		activity.trackPageView("/OpenBugEmail|" + activity.getClass().getCanonicalName());
	}

	public static void openImprovement(final GoogleAnalyticsActivity activity) {
		Utils.sendEmail(activity.getActivity(), "Send mail...", "Who's Making The Brew : Improvement",
				Constants.MY_EMAIL);
		activity.trackPageView("/OpenImprovmentEmail|" + activity.getClass().getCanonicalName());
	}

}
