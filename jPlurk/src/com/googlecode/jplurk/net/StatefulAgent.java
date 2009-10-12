package com.googlecode.jplurk.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
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
			PostMethod method, int responseCode) {
		if (HttpStatus.SC_MOVED_TEMPORARILY == responseCode) {
			Header loc = method.getResponseHeader("Location");
			if (loc != null && loc.getValue() != null) {
				logger.info("redirect to " + loc.getValue());
				return executePost(loc.getValue(), params);
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

	public Result multipartUpload(String uri, File targetFile){
		PostMethod filePost = new PostMethod(uri);
		filePost.getParams().setBooleanParameter(
				HttpMethodParams.USE_EXPECT_CONTINUE, true);

//		filePost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		filePost.setRequestHeader("Referer", "http://www.plurk.com/Shares/showPhoto?mini=0");

		Result result = new Result();
		try {
			Part[] parts = {
					new StringPart("mini", "0"),
					new FilePart("image", targetFile) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			int status = client.executeMethod(filePost);

			if (status == HttpStatus.SC_OK) {
				result.setOk(true);
				try {
					result.setResponseBody(filePost.getResponseBodyAsString());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			} else{
				result.setResponseBody(filePost.getResponseBodyAsString());
				System.out.println("res:" + status);
			}

		} catch (Exception e) {
			result.setOk(false);
		} finally {
			filePost.releaseConnection();
		}

		return result;

	}

	public Result executePost(String uri, Map<String, String> params) {
		// FIXME to avoid the user's password logged.
		logger.info("do method with uri: " + uri + " and params => " + params);
		PostMethod method = createMethod(PostMethod.class, uri);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			method.addParameter(e.getKey(), e.getValue());
		}

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

	public static void main(String[] args) {
		StatefulAgent agent = new StatefulAgent();
		final Account account = Account.createWithDynamicProperties();
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

		result = agent.multipartUpload("/Shares/uploadImage", new File("c:/tmp/base.png"));
		System.out.println(result);

	}

}
