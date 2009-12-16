package com.google.jplurk.action;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.google.jplurk.exception.PlurkException;

public final class PlurkActionSheet {

	private final static PlurkActionSheet self = new PlurkActionSheet();

	private PlurkActionSheet() {
	}

	public static PlurkActionSheet getInstance() {
		return self;
	}

	private static String getApiUri(String uri) {
		return "http://www.plurk.com/API" + uri;
	}

	@Meta(uri = "/Users/login", require = { "api_key", "username", "password" })
	public HttpRequestBase login(Map<String, String> params)
			throws PlurkException {
		return prepare("login", params);
	}

    /* TODO :
     * Optional parameters:
     * email: Must be a valid email.
     * */
    @Meta(uri="/Users/register", require = { "api_key", "nick_name", "full_name", "password", "gender", "date_of_birth" })
    public HttpRequestBase register(Map<String, String> params)
            throws PlurkException {
        return prepare("register", params);
    }

	private HttpRequestBase prepare(String methodName, Map<String, String> params) throws PlurkException {
		Method method = MethodUtils.getAccessibleMethod(PlurkActionSheet.class,
				methodName, new Class[] { Map.class });
		if (method == null) {
			throw new PlurkException("can not find the method: " + methodName);
		}

		Meta meta = method.getAnnotation(Meta.class);
		if (meta == null) {
			throw new PlurkException("can not find the meta annotation");
		}

		final StringBuffer buf = new StringBuffer();
		for (String key : params.keySet()) {
			buf.append(key).append("=").append(params.get(key)).append("&");
		}
		buf.deleteCharAt(buf.length() - 1);

		final String uri = getApiUri(meta.uri() + "?" + buf.toString());
		final HttpRequestBase httpMethod = meta.type().equals(Type.GET) ? new HttpGet(uri) : new HttpPost(uri);

		for (String key : meta.require()) {
			if (!params.containsKey(key)) {
				throw new PlurkException("require param [" + key + "] is not found");
			}
		}

		Headers headers = method.getAnnotation(Headers.class);
		if (headers != null) {
			for (Header header : headers.headers()) {
				httpMethod.addHeader(header.key(), header.value());
			}
		}

		return httpMethod;
	}

}
