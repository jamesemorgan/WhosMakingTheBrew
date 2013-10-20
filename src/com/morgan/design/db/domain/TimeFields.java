package com.morgan.design.db.domain;

public class TimeFields {

	private int minute = 0;
	private int hour = 0;
	private int second = 0;

	public int getHour() {
		return this.hour;
	}

	public int getMinute() {
		return this.minute;
	}

	public int getSecond() {
		return this.second;
	}

	public void setHour(final int hour) {
		this.hour = hour;
	}

	public void setSecond(final int second) {
		this.second = second;
	}

	public void setMinute(final int minute) {
		this.minute = minute;
	}
}
