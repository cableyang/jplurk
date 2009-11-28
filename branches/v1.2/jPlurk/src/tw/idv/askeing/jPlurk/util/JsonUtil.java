package tw.idv.askeing.jPlurk.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonUtil {

	static Log logger = LogFactory.getLog(JsonUtil.class);

	public static JSONObject parse(String jStr) {
		String patchDateObject = PatternUtils.replaceJsDateToTimestamp(jStr);
		JSONObject o = (JSONObject) JSONValue.parse(patchDateObject);
		return o;
	}

	public static JSONArray parseArray(String jStr) {
		String patchDateObject = PatternUtils.replaceJsDateToTimestamp(jStr);
		JSONArray o = (JSONArray) JSONValue.parse(patchDateObject);
		return o;
	}

	public static Map<String, String> get(JSONObject o, String... fields) {
		Map<String, String> result = new HashMap<String, String>();
		for (String f : fields) {
			try {
				Object v = o.get(f);
				logger.debug("the value of `" + f + "' is: " + v);
				result.put(f, "" + v);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

}
