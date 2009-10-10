package tw.idv.askeing.jPlurk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.NativeJavaObject;

@SuppressWarnings("restriction")
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

	/**
	 * eval javascript date object
	 * <pre>
	 * Date date = TimeUtil.fromJsDate("new Date('Sat, 04 Jul 2009 13:31:23 GMT')");
	 * </pre>
	 * @param jsDate js date object. for example: <b>new Date('Sat, 04 Jul 2009 13:31:23 GMT')</b>
	 * @return
	 */
	public static Date fromJsDate(String jsDate){
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("javascript");
		Date date = null;
		try {
			Object o = engine.eval(jsDate);
			if(NativeJavaObject.canConvert(o, Date.class)){
				date = (Date) Context.jsToJava(o, Date.class);
			}
		} catch (ScriptException ex) {
		}
		return date;
	}
}
