package tw.idv.askeing.jPlurk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.jplurk.parser.ParserExecutor;
import com.googlecode.jplurk.parser.UidParser;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.net.HttpResultCallback;
import tw.idv.askeing.jPlurk.net.HttpTemplate;

/**
 * jPlurk UIDGetter: get UID of User. If you get UID and UID != 0, then UID will store into AccountModel.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class UIDManager {

    static Log logger = LogFactory.getLog(UIDManager.class);
    static Map<String, Integer> uuids = new HashMap<String, Integer>();

    public static void set(Account account, Integer uuid){
    	uuids.put(account.getName(), uuid);
    }

    public static Integer get(Account account){
    	if(!uuids.containsKey(account.getName())){
    		uuids.put(account.getName(), fetchUid(account));
    	}
    	return uuids.get(account.getName());
    }

//    protected static Integer fetchUid(Account user) {
//		Result result = new StatefulAgent().executeGet("/" + user.getName(),
//				new HashMap<String, String>());
//		if (!result.isOk()) {
//			return 0;
//		}
//
//		try {
//			return Integer.parseInt(ParserExecutor.parseSingleValue(
//					UidParser.class, result.getResponseBody()));
//		} catch (Exception e) {
//		}
//
//		return 0;
//	}

//    private UIDManager() {
//	}
//
//    /**
//     * Return UID of user.
//     * @param user user account
//     * @return UID
//     */
//    public static int getUID(Account user) {
//    	if(isHitCache(user)){
//    		return cachedUids.get(user).intValue();
//    	}
//
//    	cachedUids.put(user, fetchUid(user));
//        return getUID(user);
//    }
//

	static Integer fetchUid(Account user) {
        GetMethod method = new GetMethod("/"+user.getName());
        method.setRequestHeader("Cookie", CookieGetter.getCookie(
                Constants.PLURK_HOST, Constants.LOGIN_URL_M, user, null));

        HttpTemplate template = new HttpTemplate(method);
        Object result = template.execute(new int[]{HttpStatus.SC_MOVED_TEMPORARILY,
                    HttpStatus.SC_OK}, new HttpResultCallback() {

            @Override
            protected Object processResult(GetMethod method) {
            	try {
//                    //return NumberUtils.toInt(StringUtils.substringBetween(method.getResponseBodyAsString(), "name=\"user_id\" value=\"", "\" />"));
//                    Iterator<String> it = getIterator(method.getResponseBodyAsStream(), "utf-8");
//                    String line = "";
//					while (it.hasNext()) {
//						line = it.next();
//                      //logger.debug(line);
//                      if (line.contains("user_id") && line.contains("show_location")) {
//                          break;
//                      }
//					}
//                    logger.debug("Get Line: "+line);
//                    logger.debug("Get ID: "+StringUtils.substringBetween(line, "\"user_id\": ", ", \"show_location\""));
//                    return NumberUtils.toInt(StringUtils.substringBetween(line, "\"user_id\": ", ", \"show_location\""));
            		return NumberUtils.toInt(ParserExecutor.parseSingleValue(UidParser.class, method.getResponseBodyAsString()));
				} catch (Exception e) {
					return 0;
				}
            }
        });

        if (result != null && result instanceof Integer) {
            return (Integer) result;
        }

        return Integer.valueOf(0);
	}
}
