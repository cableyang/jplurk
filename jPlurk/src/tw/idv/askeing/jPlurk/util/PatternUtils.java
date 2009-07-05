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

	/**
	 * the posted field in the plurk json response use <b>new Date("Sun, 05 Jul 2009 02:56:30 GMT")</b> define the date, use replaceJsDateToTimestamp method can transform it to timestamp
	 * @param input
	 * @return
	 */
	public static String replaceJsDateToTimestamp(String input){
		Matcher m = Pattern.compile("(new Date\\([^(]+\\))", Pattern.DOTALL | Pattern.MULTILINE).matcher(input);
		m.reset();
        boolean result = m.find();
        StringBuffer sb = new StringBuffer();
        if (result) {
            do {
            	System.out.println(m.group(1));
                m.appendReplacement(sb, "" + TimeUtil.fromJsDate(m.group(1)).getTime());
                result = m.find();
            } while (result);
            m.appendTail(sb);
        }
        return sb.toString();
	}


}
