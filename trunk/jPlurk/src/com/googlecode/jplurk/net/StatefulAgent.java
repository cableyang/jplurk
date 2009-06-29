package com.googlecode.jplurk.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.Account;

public class StatefulAgent {

	static Log logger = LogFactory.getLog(StatefulAgent.class);
	HttpClient client;

	public StatefulAgent() {
		client = new HttpClient();
		client.getHostConfiguration().setHost(Constants.PLURK_HOST,
				Constants.PLURK_PORT, "http");
	}

	/**
	 * Creating the http method and set cookie policy as RFC_2109 (make cookie keep in the http client automatically)
	 * @param <T>
	 * @param t
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends HttpMethod> T createMethod(Class<T> t, String uri) {
		HttpMethod method = null;
		try {
			method = t.newInstance();
			method.setPath(uri);
			method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return (T) method;
	}

	protected Result followLinkIfNeedded(Map<String, String> params,
			HttpMethod method, int responseCode) {
		if (HttpStatus.SC_MOVED_TEMPORARILY == responseCode) {
			Header loc = method.getResponseHeader("Location");
			if (loc != null && loc.getValue() != null) {
				logger.info("redirect to " + loc.getValue());
				if (method instanceof PostMethod) {
					return executePost(loc.getValue(), params);
				}else{
					return executeGet(loc.getValue(), params);
				}
			}
		}
		return null;
	}

	protected void dumpCookies() {
		if (client != null && logger.isDebugEnabled()) {
			for (Cookie c : client.getState().getCookies()) {
				logger.debug("Cookie: " + c);
			}
		}
	}

	protected void dumpResponseHeaders(HttpMethod method) {
		if (method != null && logger.isDebugEnabled()) {
			logger.debug("uri: " + method.getPath());
			for (Header h : method.getResponseHeaders()) {
				logger.debug(h.getName() + " : " + h.getValue());
			}
		}
	}

	public Result executeGet(String uri, Map<String, String> params) {
		logger.info("doGet method with uri: " + uri + " and params => " + params);
		DoMethodStrategy<GetMethod> doMethod = new DoMethodStrategy<GetMethod>() {
			@Override
			void configureGet(Map<String, String> params) {
				Iterator<Entry<String, String>> it = params.entrySet().iterator();
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					nvps.add(new NameValuePair(e.getKey(), e.getValue()));
				}
				if (!nvps.isEmpty()) {
					method.setQueryString(nvps.toArray(new NameValuePair[0]));
				}
			}
		};

		doMethod.create(GetMethod.class);
		doMethod.applyParams(params);
		return doMethod.execute(client, params);
	}

	public Result executePost(String uri, Map<String, String> params) {
		logger.info("doPost method with uri: " + uri + " and params => " + params);
		DoMethodStrategy<PostMethod> doMethod = new DoMethodStrategy<PostMethod>() {
			@Override
			void configurePost(Map<String, String> params) {
				method.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");

				Iterator<Entry<String, String>> it = params.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					method.addParameter(e.getKey(), e.getValue());
				}
			}
		};
		doMethod.create(PostMethod.class);
		doMethod.applyParams(params);
		return doMethod.execute(client, params);

	}

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		StatefulAgent agent = new StatefulAgent();
		final Account account = new Account();
		Result result = null;
		result = agent.executePost("/Users/login",
				new HashMap<String, String>() {
					{
						put("nick_name", account.getName());
						put("password", account.getPassword());
					}
				});

		System.out.println(result);

		result = agent.executePost("/TimeLine/getPlurks",
				new HashMap<String, String>() {
					{
						put("user_id", "3146394");
					}
				});
		System.out.println(result);

		agent.executePost("", new HashMap<String, String>());
	}


	/**
	 * Execute GetMethod and PostMethod is similiar but a little different at setting params.
	 * encapsulate the process and leave the configure abstract to customize.
	 * @author Ching Yi, Chan
	 *
	 * @param <T>
	 */
	class DoMethodStrategy<T extends HttpMethod> {

		T method;

		public void create(Class<T> t){
			try {
				method = t.newInstance();
			} catch (Exception e) {
			}
		}

		public void applyParams(Map<String, String> params){
			if (method instanceof PostMethod) {
				configurePost(params);
			}
			if (method instanceof GetMethod) {
				configureGet(params);
			}
		}

		/**
		 * when doPost should override this method to implement params configuration.
		 * @param params
		 */
		void configurePost(Map<String, String> params) {
			throw new UnsupportedOperationException("this metod is not yet implemented");
		}

		/**
		 * when doGet should override this method to implement params configuration.
		 * @param params
		 */
		void configureGet(Map<String, String> params) {
			throw new UnsupportedOperationException("this metod is not yet implemented");
		}

		public Result execute(HttpClient client, Map<String, String> params){
			int responseCode = 0;
			try {
				responseCode = client.executeMethod(method);
				logger.info("Request Response Code: " + responseCode);
				dumpCookies();
				dumpResponseHeaders(method);
				Result redirect = followLinkIfNeedded(params, method, responseCode);
				if (redirect != null) {
					return redirect;
				}
				if (responseCode != HttpStatus.SC_OK) {
					return Result.FAILURE_RESULT;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			Result result = new Result();
			result.setOk(true);
			try {
				result.setResponseBody(method.getResponseBodyAsString());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			return result;
		}
	}




}
