package tw.idv.askeing.jPlurk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public final static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");

	public static String format(Date date) {
		return format.format(date);
	}

	public static String format(Calendar calendar) {
		return format(calendar.getTime());
	}

	public static String now() {
		return format(new Date());
	}
}
