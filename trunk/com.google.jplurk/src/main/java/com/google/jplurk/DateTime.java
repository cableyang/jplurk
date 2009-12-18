package com.google.jplurk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {

	public final static SimpleDateFormat OFFSET_OUTPUT_FORMAT =
		new SimpleDateFormat("yyyy-M-d'T'HH:mm:ss", Locale.US);

	int year;
	int month;
	int day;
	int hour;
	int minute;
	int second;

	private DateTime(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	private DateTime(int year, int month, int day,
		int hour, int minute, int second) {

		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public static DateTime create(int year, int month, int day){
		return new DateTime(year, month, day);
	}

	public static DateTime create(int year, int month, int day,
			int hour, int minute, int second) {
		return new DateTime(year, month, day, hour, minute, second);
	}

	public static DateTime now() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return new DateTime(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH + 1),
			c.get(Calendar.DAY_OF_MONTH),
			c.get(Calendar.HOUR_OF_DAY),
			c.get(Calendar.MINUTE),
			c.get(Calendar.SECOND));
	}

	protected String birthday() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(c.getTime());
	}

	protected String timeOffset() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return OFFSET_OUTPUT_FORMAT.format(c.getTime());
	}

	public static boolean isValidTimeOffset(String offset) {
		try {
			OFFSET_OUTPUT_FORMAT.parse(offset);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
