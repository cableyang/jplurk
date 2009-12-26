package com.google.jplurk.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.jplurk.action.Headers.Header;
import com.google.jplurk.action.Validation.Validator;
import com.google.jplurk.exception.PlurkException;
import com.google.jplurk.validator.EmailValidator;
import com.google.jplurk.validator.IDListValidator;
import com.google.jplurk.validator.IValidator;
import com.google.jplurk.validator.NonNegativeIntegerValidator;
import com.google.jplurk.validator.PositiveIntegerValidator;
import com.google.jplurk.validator.QualifierValidator;
import com.google.jplurk.validator.TimeOffsetValidator;

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

	@Meta(uri = "/Alerts/addAllAsFriends", require = { "api_key" })
	public HttpUriRequest addAllAsFriends(Map<String, String> params)
			throws PlurkException {
		return prepare("addAllAsFriends", params);
	}

	@Meta(uri = "/FriendsFans/getFriendsByOffset", require = { "api_key", "user_id" })
	@Validation({ @Validator(field = "offset", validator = NonNegativeIntegerValidator.class) })
	public HttpUriRequest getFriendsByOffset(Map<String, String> params)
			throws PlurkException {
		return prepare("getFriendsByOffset", params);
	}

	@Meta(uri = "/FriendsFans/getFansByOffset", require = { "api_key", "user_id" })
	@Validation({ @Validator(field = "offset", validator = NonNegativeIntegerValidator.class) })
	public HttpUriRequest getFansByOffset(Map<String, String> params)
			throws PlurkException {
		return prepare("getFansByOffset", params);
	}

	@Meta(uri = "/Users/login", require = { "api_key", "username", "password" })
	public HttpUriRequest login(Map<String, String> params)
			throws PlurkException {
		return prepare("login", params);
	}

	@Meta(uri = "/Users/register",
		require = { "api_key", "nick_name", "full_name", "password", "gender", "date_of_birth" })
	@Validation({ @Validator(field = "email", validator = EmailValidator.class) })
    public HttpUriRequest register(Map<String, String> params)
            throws PlurkException {
        return prepare("register", params);
    }

	@Meta(uri = "/Users/update", require = { "api_key", "current_password" })
	@Validation( { @Validator(field = "email", validator = EmailValidator.class), })
	public HttpUriRequest update(Map<String, String> params)
			throws PlurkException {
		return prepare("update", params);
	}
	
	@Meta(uri = "/Users/updatePicture", require = { "api_key", "profile_image" }, type = Type.POST)
	@Headers(headers = { @Header(key = "Content-Type", value = "multipart/form-data") })
	public HttpUriRequest updatePicture(Map<String, String> params)
			throws PlurkException {
		return prepare("updatePicture", params);
	}	
	
    @Meta(uri = "/Timeline/getPlurk", require = { "api_key", "plurk_id" })
    @Validation({
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest getPlurk(Map<String, String> params) throws PlurkException{
		return prepare("getPlurk", params);
	}

    @Meta(uri = "/Timeline/getPlurks", require = { "api_key"})
	@Validation({
		@Validator(field = "offset", validator = TimeOffsetValidator.class),
		@Validator(field = "limit", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest getPlurks(Map<String, String> params) throws PlurkException{
		return prepare("getPlurks", params);
	}

	@Meta(uri = "/Timeline/plurkAdd", require = { "api_key", "content", "qualifier" })
	@Validation(value = {
			@Validator(field = "limited_to", validator = IDListValidator.class),
			@Validator(field = "qualifier", validator = QualifierValidator.class) })
	public HttpUriRequest plurkAdd(Map<String, String> params) throws PlurkException{
		return prepare("plurkAdd", params);
	}

	@Meta(uri = "/Timeline/getUnreadPlurks", require = { "api_key" })
	@Validation({
		@Validator(field = "offset", validator = TimeOffsetValidator.class),
		@Validator(field = "limit", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest getUnreadPlurks(Map<String, String> params) throws PlurkException{
		return prepare("getUnreadPlurks", params);
	}

    @Meta(uri = "/Timeline/plurkDelete", require = { "api_key", "plurk_id" })
    @Validation({
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
    public HttpUriRequest plurkDelete(Map<String, String> params) throws PlurkException {
        return prepare("plurkDelete", params);
    }

    @Meta(uri = "/Timeline/plurkEdit", require = { "api_key", "plurk_id", "content" })
    @Validation({
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
    public HttpUriRequest plurkEdit(Map<String, String> params) throws PlurkException {
        return prepare("plurkEdit", params);
    }

	@Meta(uri = "/Timeline/mutePlurks", require = { "api_key", "ids" })
	@Validation({@Validator(field = "ids", validator = IDListValidator.class)})
	public HttpUriRequest mutePlurks(Map<String, String> params) throws PlurkException {
		return prepare("mutePlurks", params);
	}

	@Meta(uri = "/Timeline/unmutePlurks", require = { "api_key", "ids" })
	@Validation({@Validator(field = "ids", validator = IDListValidator.class)})
	public HttpUriRequest unmutePlurks(Map<String, String> params) throws PlurkException {
		return prepare("unmutePlurks", params);
	}

	@Meta(uri = "/Timeline/markAsRead", require = { "api_key", "ids" })
	@Validation({@Validator(field = "ids", validator = IDListValidator.class)})
	public HttpUriRequest markAsRead(Map<String, String> params) throws PlurkException {
		return prepare("markAsRead", params);
	}

	@Meta(uri = "/Responses/get", require = { "api_key", "plurk_id", "from_response" })
	@Validation({
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest responseGet(Map<String, String> params) throws PlurkException {
		return prepare("responseGet", params);
	}

	@Meta(uri = "/Responses/responseAdd", require = { "api_key", "content", "qualifier", "plurk_id" })
	@Validation({
		@Validator(field = "qualifier", validator = QualifierValidator.class),
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest responseAdd(Map<String, String> params) throws PlurkException {
		return prepare("responseAdd", params);
	}

    @Meta(uri = "/Responses/responseDelete", require = { "api_key", "response_id", "plurk_id" })
    @Validation({
		@Validator(field = "response_id", validator = PositiveIntegerValidator.class),
		@Validator(field = "plurk_id", validator = PositiveIntegerValidator.class)
	})
	public HttpUriRequest responseDelete(Map<String, String> params) throws PlurkException {
		return prepare("responseDelete", params);
	}

	@Meta(uri = "/Timeline/uploadPicture", require = { "api_key", "image" }, type = Type.POST)
	@Headers(headers = { @Header(key = "Content-Type", value = "multipart/form-data") })
	public HttpUriRequest uploadPicture(Map<String, String> params)
			throws PlurkException {
		return prepare("uploadPicture", params);
	}

	@Meta(uri = "/Profile/getOwnProfile", require = { "api_key" })
	public HttpUriRequest getOwnProfile(Map<String, String> params)
			throws PlurkException {
		return prepare("getOwnProfile", params);
	}

	@Meta(uri = "/Profile/getPublicProfile", require = { "api_key", "user_id" })
	public HttpUriRequest getPublicProfile(Map<String, String> params)
			throws PlurkException {
		return prepare("getPublicProfile", params);
	}

	private HttpUriRequest prepare(String methodName, Map<String, String> params) throws PlurkException {
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
			try {
				buf.append(key).append("=").append(URLEncoder.encode(params.get(key), "utf-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
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
			for (Validator v : validation.value()) {
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

		if (logger.isInfoEnabled()) {
			Map<String, String> loggedParams = new HashMap<String, String>(params);
			for (String key : loggedParams.keySet()) {
				if(key.contains("key") || key.contains("password")){
					loggedParams.put(key, "**********");
				}
			}
			logger.info("Params: " + loggedParams.toString());
		}

		return httpMethod;
	}


}
