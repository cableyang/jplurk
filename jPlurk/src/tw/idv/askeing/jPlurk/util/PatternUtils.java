package tw.idv.askeing.jPlurk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

	final static int FLAG = Pattern.DOTALL | Pattern.MULTILINE;

	public static String getPropertyWithIntValue(String input, String prop) {
		Pattern patt = Pattern.compile(".*" + prop + "[^:]+:\\s*(\\d+).*", FLAG);
		Matcher m = patt.matcher(input);
		if (m.matches()) {
			return m.group(1);
		}
		return "";
	}

}
