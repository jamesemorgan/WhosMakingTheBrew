package com.morgan.design.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.RemoteViews;

import com.morgan.design.R;

public class NotifcationUtils {

	/**
	 * @param packageName
	 * @param layoutId
	 * @return {@link RemoteViews}
	 */
	public static RemoteViews createRemoteViews(final String packageName, final int layoutId) {
		return new RemoteViews(packageName, layoutId);
	}

	/**
	 * Set the icon, scrolling text and timestamp
	 */
	public static Notification createTeaTimerNotifcation(final CharSequence charSequence) {
		return new Notification(R.drawable.dashboard_teapot_icon, charSequence, System.currentTimeMillis());
	}

	/**
	 * Set the icon, scrolling text and timestamp
	 * 
	 * @param context
	 * @param titleText
	 *            string.xml ID
	 * @return {@link Notification}
	 */
	public static Notification createTeaTimerNotifcation(final Context context, final int titleText) {
		return new Notification(R.drawable.dashboard_teapot_icon, context.getText(titleText),
				System.currentTimeMillis());
	}

	/**
	 * Get the notification manager service.
	 * 
	 * @param context
	 * @return {@link NotificationManager}
	 */
	public static NotificationManager getManager(final ContextWrapper context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
}
