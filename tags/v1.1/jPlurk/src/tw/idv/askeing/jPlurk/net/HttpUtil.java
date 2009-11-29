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
import tw.idv.askeing.jPlurk.model.Account;

public class HttpUtil {

	final static Pattern SET_COOKIE_PATTERN = Pattern.compile("^([^;]+).*");
	static Log logger = LogFactory.getLog(HttpUtil.class);

	static Map<String, String> COOKIE_NAMEKEY_URI_MAP = new HashMap<String, String>();

	static {
		COOKIE_NAMEKEY_URI_MAP.put("/m/login", "username");
		COOKIE_NAMEKEY_URI_MAP.put("/Users/login", "nick_name");
	}

	/**
	 * if request uri is /m/login set nameKey as username
	 * @param user
     * @param uri
	 * @param optCookie
	 * @return
	 */
	public static PostMethod createGetCookieHttpMethod(Account user,
			String uri, String optCookie) {
		PostMethod method = new PostMethod(uri);
		try {
			if (!COOKIE_NAMEKEY_URI_MAP.containsKey(method.getURI().toString())) {
				logger.info("use optional cookie directly");
				method.addParameter(new NameValuePair("Cookie", optCookie));
				return method;
			}

			logger.info("login to url[" + method.getURI()
				+ "] with nameKey["	+ COOKIE_NAMEKEY_URI_MAP.get(method.getURI().toString()) + "] ");

			method.addParameter(new NameValuePair(
				COOKIE_NAMEKEY_URI_MAP.get(method.getURI().toString()), user.getName()));
			logger.info("set parameter for: " + COOKIE_NAMEKEY_URI_MAP.get(method.getURI().toString()));
			method.addParameter(new NameValuePair("password", user.getPassword()));

			logger.info("finish to set usernamd and password");
		} catch (URIException e) {
			logger.error(e.getMessage(), e);
		}

		return method;
	}

    /**
     * Return Cookie.
     * @param headers
     * @return
     */
    public static String parseSetCookieHeader(Header[] headers){
		for (Header header : headers) {
			if(!RFC2109Spec.SET_COOKIE_KEY.equalsIgnoreCase(header.getName())){
                continue;
			}

			logger.info(header.getValue());
			// TODO: 有需要判斷 cookie key (plurkcookie) 嗎 ?
			Matcher matcher = SET_COOKIE_PATTERN.matcher(header.getValue());
			if(matcher.matches()){
				return matcher.group(1);
			}
		}
		return "";
	}

    /**
     * Creat Default Http Client.
     * @return
     */
    public static HttpClient createDefaultHttpClient(){
		return createHttpClient(Constants.PLURK_HOST, Constants.PLURK_PORT);
	}

    /**
     * Creat Http Client with host and port.
     * @param host
     * @param port
     * @return
     */
    public static HttpClient createHttpClient(String host, int port){
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, 80, "http");
		return client;
	}
}
