package com.google.jplurk.action;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.jplurk.action.Headers.Header;
import com.google.jplurk.action.Validation.Validators;
import com.google.jplurk.exception.PlurkException;
import com.google.jplurk.validator.EmailValidator;
import com.google.jplurk.validator.IValidator;

public final class PlurkActionSheet {

	static Logger logger = LoggerFactory.getLogger(PlurkActionSheet.class);
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

	@Meta(uri = "/Users/register",
		require = { "api_key", "nick_name", "full_name", "password", "gender", "date_of_birth" })
	@Validation({ @Validators(field = "email", validator = EmailValidator.class) })
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
			logger.debug("found @Headers");
			for (Header header : headers.headers()) {
				logger.debug("add header => name[" + header.key() + "] value[" + header.value() + "]");
				httpMethod.addHeader(header.key(), header.value());
			}
		}

		Validation validation = method.getAnnotation(Validation.class);
		if (validation != null) {
			logger.debug("found @Validation");
			for (Validators v : validation.value()) {
				if (params.containsKey(v.field())) {
					logger.debug("validate field[" + v.field() + "]");
					boolean isPass = IValidator.ValidatorUtils.validate(v.validator(), params.get(v.field()));
					if(!isPass){
						throw new PlurkException(
							"validation failure. the field ["
							+ v.field() + "] can not pass validation [" + v.validator() + "]");
					}
				}
			}
		}

		return httpMethod;
	}

}
