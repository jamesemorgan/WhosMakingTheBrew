package com.morgan.design.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.morgan.design.R;
import com.morgan.design.activity.CreditsFragmentActivity;
import com.morgan.design.activity.FeedbackFormFragmentActivity;

public class Utils {

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

    public static void openCredits(final Activity activity) {
        try {
            activity.startActivity(new Intent(activity, CreditsFragmentActivity.class));
        } catch (final Exception e) {
            Utils.shortToast(activity, "Unable to open credits");
        }
    }

    public static void openFeedback(final Activity activity) {
        try {
            activity.startActivity(new Intent(activity, FeedbackFormFragmentActivity.class));
        } catch (final Exception e) {
            Utils.shortToast(activity, "Unable to open feedback");
        }
    }

    public static void openUrl(final Activity activity, final String url) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (final Exception e) {
            Utils.shortToast(activity, "Unable to open URL");
        }
    }

    private static void shortToast(final Activity activity, final CharSequence message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
