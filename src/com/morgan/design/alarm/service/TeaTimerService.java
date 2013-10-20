package com.morgan.design.alarm.service;

import static com.morgan.design.TeaApplication.TEA_TIMER_RUNNING_NOTIFICATION;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.morgan.design.R;
import com.morgan.design.TeaApplication;
import com.morgan.design.TeaTimerActivity;
import com.morgan.design.helpers.Logger;
import com.morgan.design.utils.ClockUtils;
import com.morgan.design.utils.NotifcationUtils;

public class TeaTimerService extends Service {

	private static final String LOG_TAG = "TeaTimerService";

	public static final String TOTAL_SECONDS = "com.morgan.design.broadcaster.teatimer.TotalSeconds";
	public static final String START_TIME = "com.morgan.design.broadcaster.teatimer.StartTime";
	public static final String SECONDS_REMAINING = "com.morgan.design.broadcaster.teatimer.SecondsRemaining";
	public static final String FORMAT_INTO_HHMMSS = "com.morgan.design.broadcaster.teatimer.FormatIntoHHMMSS";

	// Clock stuff
	private Handler timeHandler;
	private TeaTimer teaTimer;

	private int secondsRemaining = 0;
	private int totalSeconds = 0;
	private long totalMills = 0L;
	private long startTime = 0L;

	private String formatIntoHHMMSS;

	private Intent intent;

	// Notification Stuff
	private Notification runningNotification;
	private RemoteViews runningRemoteView;

	@Override
	public void onCreate() {
		this.timeHandler = new Handler();
		this.teaTimer = new TeaTimer(this.timeHandler);
		this.intent = new Intent(TeaApplication.TEA_TIMER_BROADCAST_ACTION);
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.d(LOG_TAG, "Service onDestroy called");
		stopAllRunning(true);
	}

	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
		stopAllRunning(false);

		// Post UI update every 1 Second
		this.timeHandler.postDelayed(this.sendUpdatesToUI, 1000);

		this.totalSeconds = intent.getIntExtra(TOTAL_SECONDS, 0);
		this.secondsRemaining = this.totalSeconds;
		this.totalMills = this.totalSeconds * 1000;
		this.startTime = intent.getLongExtra(START_TIME, 0);

		// Log.d(BROADCAST_ACTION, totalMills);
		// Log.d(BROADCAST_ACTION, startTime);

		this.teaTimer.run();
		createInitialNotification();
	}

	public void createInitialNotification() {
		this.runningNotification = NotifcationUtils.createTeaTimerNotifcation(this);
		this.runningRemoteView = NotifcationUtils.createRemoteViews(getPackageName(), TEA_TIMER_RUNNING_NOTIFICATION);
		this.runningRemoteView.setImageViewResource(R.id.custom_notification_image, R.drawable.dashboard_teapot_icon);
		this.runningRemoteView.setTextViewText(R.id.custom_notification_text_upper,
				getText(R.string.tea_timer_status_bar_notifications_text));
		this.runningNotification.contentView = this.runningRemoteView;

		final Intent notificationIntent = new Intent(this, TeaTimerActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		this.runningNotification.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		this.runningNotification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.DEFAULT_LIGHTS;

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		NotifcationUtils.getManager(this).notify(TEA_TIMER_RUNNING_NOTIFICATION, this.runningNotification);
	}

	private void updateNotification(final String countdownText) {
		this.runningRemoteView.setTextViewText(R.id.custom_notification_text_lower, countdownText);
		NotifcationUtils.getManager(this).notify(TEA_TIMER_RUNNING_NOTIFICATION, this.runningNotification);
	}

	private void sendUpdateBroadcastAndNotify() {
		if (this.secondsRemaining < 0) {
			stopAllRunning(true);
		}
		else {
			this.intent.putExtra(SECONDS_REMAINING, this.secondsRemaining);
			this.intent.putExtra(FORMAT_INTO_HHMMSS, this.formatIntoHHMMSS);
			this.intent.putExtra(TOTAL_SECONDS, this.totalSeconds);
			updateNotification(this.formatIntoHHMMSS);
			sendBroadcast(this.intent);
		}
	}

	private void stopAllRunning(final boolean cancelNotifcations) {
		if (cancelNotifcations) {
			NotifcationUtils.getManager(this).cancel(TEA_TIMER_RUNNING_NOTIFICATION);
		}
		this.timeHandler.removeCallbacks(this.teaTimer);
		this.timeHandler.removeCallbacks(this.sendUpdatesToUI);
		this.teaTimer.stopHandler();
	}

	private final Runnable sendUpdatesToUI = new Runnable() {
		@Override
		public void run() {
			sendUpdateBroadcastAndNotify();
			TeaTimerService.this.timeHandler.postDelayed(this, 1000);
		}
	};

	private class TeaTimer implements Runnable {
		final Handler handler;

		public TeaTimer(final Handler handler) {
			this.handler = handler;
		}

		public void stopHandler() {
			this.handler.removeCallbacks(this);
		}

		@Override
		public void run() {
			// rT = sT � cT + duration
			// where
			// rT = remaining time. The time remaining to count down (in
			// milliseconds)
			// sT = start time. The time at which the timer started
			// cT = current time
			// duration = duration. The duration of the time. Example 20 minutes
			// (In milliseconds: 20 * 60 * 1000)

			final long remaining = (TeaTimerService.this.startTime - System.currentTimeMillis())
					+ TeaTimerService.this.totalMills;
			TeaTimerService.this.formatIntoHHMMSS = ClockUtils.formatIntoHHMMSS(remaining / 1000);
			TeaTimerService.this.secondsRemaining--;

			// Log.d(LOG_TAG, "Fromatted Time: " + formatIntoHHMMSS);
			// Log.d(LOG_TAG, "Mills Remaining: " + remaining);
			// Log.d(LOG_TAG, "Seconds Remaining: " + totalSeconds);

			if (remaining > 0) {
				// Making sure that there is only one Runnable in the message
				// queue
				this.handler.removeCallbacks(this);

				// Repost this Runnable with a delay
				this.handler.postDelayed(this, 1000);
			}
			else {
				stopAllRunning(true);
			}
		}
	}
}
