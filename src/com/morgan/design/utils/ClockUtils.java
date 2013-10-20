package com.morgan.design.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.morgan.design.ui.NumberPicker;

public class ClockUtils {

	public static int hours(final NumberPicker npHours) {
		return npHours.getCurrent() * 60 * 60;
	}

	public static int minutes(final NumberPicker npMinutes) {
		return npMinutes.getCurrent() * 60;
	}

	public static int seconds(final NumberPicker npSeconds) {
		return npSeconds.getCurrent();
	}

	public static String formatIntoHHMMSS(final long secsIn) {
		final long hours = secsIn / 3600;
		final long remainder = secsIn % 3600;
		final long minutes = remainder / 60;
		final long seconds = remainder % 60;
		return (zeroPad(hours) + hours + ":" + zeroPad(minutes) + minutes + ":" + zeroPad(seconds) + seconds);
	}

	private static String zeroPad(final long interval) {
		return (interval < 10
				? "0"
				: "");
	}

	public static String formatTime(final long totalSeconds, final int timer) {
		if (timer == 0) {
			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			return sdf.format(new Date(totalSeconds * 1000));
		}
		else {

			String seconds = Integer.toString((int) (totalSeconds % 60));
			String minutes = Integer.toString((int) (totalSeconds / 60));
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			return minutes + ":" + seconds;
		}
	}
}
