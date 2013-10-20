package com.morgan.design;

import static com.morgan.design.alarm.service.TeaTimerService.FORMAT_INTO_HHMMSS;
import static com.morgan.design.alarm.service.TeaTimerService.SECONDS_REMAINING;
import static com.morgan.design.alarm.service.TeaTimerService.START_TIME;
import static com.morgan.design.alarm.service.TeaTimerService.TOTAL_SECONDS;
import static com.morgan.design.utils.ObjectUtils.isNotNull;
import static com.morgan.design.utils.ObjectUtils.isZero;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.morgan.design.analytics.AbstractActivityAnalytic;
import com.morgan.design.db.domain.TimeFields;
import com.morgan.design.helpers.Constants;
import com.morgan.design.helpers.Logger;
import com.morgan.design.ui.NumberPicker;
import com.morgan.design.utils.ClockUtils;
import com.morgan.design.utils.PreferencesUtils;
import com.morgan.design.utils.Utils;

public class TeaTimerActivity extends AbstractActivityAnalytic {

	// http://www.technologichron.net/?p=42
	// http://blog.sptechnolab.com/2011/02/10/android/android-countdown-timer/
	// http://blog.sptechnolab.com/2011/01/17/android/simple-progress-bar-in-android-using-thread/
	// http://groups.google.com/group/android-developers/browse_thread/thread/5baf5a3eaa823b7b

	private final String LOG_TAG = "TimerActivity";

	// Number pickers
	private NumberPicker npHours;
	private NumberPicker npMinutes;
	private NumberPicker npSeconds;

	// Progress Bar
	private ProgressBar progressBar;

	private TextView countDownText;

	// Clock stuff
	private int totalSeconds = 0;
	private long startTime = 0L;

	private Intent teaTimerIntent;
	private BroadcastReceiver broadcastReceiver;

	private TeaApplication application;
	private boolean isRunning = false;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tea_timer);
		this.application = (TeaApplication) this.getApplication();

		findAllViewsById();

		setUpTeaTimerService();
		setUpBroadcastReceiver();
		setUpTimerRange();
		setSharedPrefs();
	}

	private void createSharedPrefs() {
		final TimeFields timeFields = new TimeFields();
		timeFields.setHour(this.npHours.getCurrent());
		timeFields.setMinute(this.npMinutes.getCurrent());
		timeFields.setSecond(this.npSeconds.getCurrent());
		PreferencesUtils.setTimerFields(this, timeFields);
	}

	private void setSharedPrefs() {
		final TimeFields timerFields = PreferencesUtils.getTimerFields(this);
		this.npHours.setCurrent(timerFields.getHour());
		this.npMinutes.setCurrent(timerFields.getMinute());
		this.npSeconds.setCurrent(timerFields.getSecond());
	}

	@Override
	protected void onResume() {
		setSharedPrefs();
		registerReceiverIfNotNull();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		createSharedPrefs();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		createSharedPrefs();
		unregisterReceiverIfNotNull();
		super.onPause();
	}

	public void onStopTimer(final View v) {
		trackEvent("Clicks", "Button", "TimerStopped", 1);
		stopServiceIfRunning();
		unregisterReceiverIfRunning();
		reset(false);
	}

	public void onResetTimer(final View v) {
		trackEvent("Clicks", "Button", "TimerReset", 1);
		stopServiceIfRunning();
		unregisterReceiverIfRunning();
		resetHourMinSecNumberPicker();
		reset(true);
	}

	public void onStartTimer(final View v) {
		trackEvent("Clicks", "Button", "TimerStarted", 1);

		this.totalSeconds = getTotalSecondsCountdown();
		// Log.d(LOG_TAG, "totalSeconds : " + Integer.toString(totalSeconds));
		// Log.d(LOG_TAG, "total time selected = " +
		// Utils.formatIntoHHMMSS(totalSeconds));

		if (isZero(this.totalSeconds)) {
			Utils.shortToast(this, "Nothing to time...");
			return;
		}

		if (isZero(this.startTime)) {
			this.progressBar.setProgress(0);
			this.progressBar.setMax(this.totalSeconds);
			this.startTime = System.currentTimeMillis();

			this.teaTimerIntent.putExtra(TOTAL_SECONDS, this.totalSeconds);
			this.teaTimerIntent.putExtra(START_TIME, this.startTime);

			startServiceIfNotNull();
		}
	}

	private void setUpTeaTimerService() {
		this.teaTimerIntent = this.application.getTeaTimerIntenet();
	}

	private void setUpBroadcastReceiver() {
		this.broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				TeaTimerActivity.this.isRunning = true;
				final String time = intent.getStringExtra(FORMAT_INTO_HHMMSS);

				// Log.d(LOG_TAG + " BroadcastReceiver time    ", time);
				// Log.d(LOG_TAG + " BroadcastReceiver remin   ", remin);

				// Update text
				TeaTimerActivity.this.countDownText.setText(time);

				// Update progress bar
				final int totalSeconds = intent.getIntExtra(TOTAL_SECONDS, 0);
				final int secondsRemaining = intent.getIntExtra(SECONDS_REMAINING, 0);
				TeaTimerActivity.this.progressBar.setMax(totalSeconds);
				TeaTimerActivity.this.progressBar.setProgress(totalSeconds - secondsRemaining);

				if (time.equalsIgnoreCase(Constants.DEFAULT_BLANK_TIME)) {
					vibrateOnComplete();
					TeaTimerActivity.this.progressBar.setProgress(totalSeconds);
					TeaTimerActivity.this.isRunning = false;
					reset(false);
				}
			}
		};
	}

	private void vibrateOnComplete() {
		final boolean vibrateOnComplete = PreferencesUtils.getTimerVibrationPref(this);
		Logger.d(this.LOG_TAG, "VibrateOnComplete : " + vibrateOnComplete);
		if (vibrateOnComplete) {
			Utils.getVibrator(this).vibrate(1000L);
		}
	}

	private void setUpTimerRange() {
		resetRanges();
		resetSpeed();
		resetHourMinSecNumberPicker();
		resetFormat();
		reset(true);
	}

	private int getTotalSecondsCountdown() {
		return ClockUtils.hours(this.npHours) + ClockUtils.minutes(this.npMinutes) + ClockUtils.seconds(this.npSeconds);
	}

	private void resetFormat() {
		this.npHours.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		this.npMinutes.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		this.npSeconds.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
	}

	private void resetRanges() {
		this.npHours.setRange(0, 23);
		this.npMinutes.setRange(0, 59);
		this.npSeconds.setRange(0, 59);
	}

	private void resetSpeed() {
		this.npHours.setSpeed(50);
		this.npMinutes.setSpeed(50);
		this.npSeconds.setSpeed(50);
	}

	private void reset(final boolean clearProgress) {
		this.startTime = 0L;
		if (clearProgress) {
			this.progressBar.setProgress(0);
			this.countDownText.setText(Constants.DEFAULT_BLANK_TIME);
		}
	}

	private void resetHourMinSecNumberPicker() {
		this.npHours.setCurrent(00);
		this.npMinutes.setCurrent(00);
		this.npSeconds.setCurrent(00);
	}

	private void findAllViewsById() {
		this.npHours = (NumberPicker) findViewById(R.id.npHours);
		this.npMinutes = (NumberPicker) findViewById(R.id.npMinutes);
		this.npSeconds = (NumberPicker) findViewById(R.id.npSeconds);
		this.countDownText = (TextView) findViewById(R.id.timer_countdown_text);
		this.progressBar = (ProgressBar) findViewById(R.id.timer_progress_bar);
	}

	private void registerReceiverIfNotNull() {
		if (isNotNull(this.broadcastReceiver)) {
			registerReceiver(this.broadcastReceiver, new IntentFilter(TeaApplication.TEA_TIMER_BROADCAST_ACTION));
		}
	}

	private void unregisterReceiverIfNotNull() {
		if (isNotNull(this.broadcastReceiver)) {
			unregisterReceiver(this.broadcastReceiver);
		}
	}

	private void startServiceIfNotNull() {
		if (isNotNull(this.teaTimerIntent) && !isRunning()) {
			startService(this.teaTimerIntent);
			this.isRunning = true;
		}
	}

	private void stopServiceIfRunning() {
		if (isNotNull(this.teaTimerIntent) && isRunning()) {
			stopService(this.teaTimerIntent);
			this.isRunning = false;
		}
	}

	private void unregisterReceiverIfRunning() {
		if (isNotNull(this.broadcastReceiver) && isRunning()) {
			unregisterReceiver(this.broadcastReceiver);
			this.isRunning = false;
		}
	}

	private final boolean isRunning() {
		return this.isRunning;
	}
}
