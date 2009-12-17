package com.google.jplurk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PlurkUtils {

	public static String birthday(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(c.getTime());
	}

}
