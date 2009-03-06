package tw.idv.askeing.jPlurk.net;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.RFC2109Spec;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.AccountModel;

public class HttpUtil {

	final static Pattern SET_COOKIE_PATTERN = Pattern.compile("^([^;]+).*");
	static Log logger = LogFactory.getLog(HttpUtil.class);

	static Map<String, String> COOKIE_NAMEKEY_URI_MAP = null;

	static {
		COOKIE_NAMEKEY_URI_MAP = new HashMap<String, String>();
		COOKIE_NAMEKEY_URI_MAP.put("/m/login", "username");
		COOKIE_NAMEKEY_URI_MAP.put("/Users/login", "nick_name");
	}

	/**
	 * if request uri is /m/login set nameKey as  username
	 * @param user
	 * @param postMethod
	 * @param optCookie
	 * @return
	 */
	public static PostMethod createGetCookieHttpMethod(AccountModel user,
			String uri, String optCookie) {
		PostMethod method = new PostMethod(uri);
		try {
			if (!COOKIE_NAMEKEY_URI_MAP.containsKey(method.getURI().toString())) {
				logger.info("use optional cookie directly");
				method.addParameter(new NameValuePair("Cookie", optCookie));
				return method;
			}

			logger.info("login to url[" + method.getURI()
				+ "] with nameKey["	+ COOKIE_NAMEKEY_URI_MAP.get(method.getURI()) + "] ");

			method.addParameter(new NameValuePair(
				COOKIE_NAMEKEY_URI_MAP.get(method.getURI().toString()), user.getName()));

			method.addParameter(new NameValuePair("password", user.getPassword()));

		} catch (URIException e) {
			logger.error(e.getMessage(), e);
		}

		return method;
	}

	public static String parseSetCookieHeader(Header[] headers){
		for (Header header : headers) {
			if(!RFC2109Spec.SET_COOKIE_KEY.equalsIgnoreCase(header.getName())){
				continue;
			}

			Matcher matcher = SET_COOKIE_PATTERN.matcher(header.getValue());
			if(matcher.matches()){
				return matcher.group(1);
			}
		}
		return "";
	}

	public static HttpClient createDefaultHttpClient(){
		return createHttpClient(Constants.PLURK_HOST, Constants.PLURK_PORT);
	}

	public static HttpClient createHttpClient(String host, int port){
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, 80, "http");
		return client;
	}
}
