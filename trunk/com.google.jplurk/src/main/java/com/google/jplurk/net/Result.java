package com.google.jplurk.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Result {

	public final static Result FAILURE = new Result("", false);
	private static Logger logger = LoggerFactory.getLogger(Result.class);
	private boolean success;
	private String body;

	public Result(String body, boolean success) {
		super();
		this.success = success;
		this.body = body;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getBody() {
		return body;
	}

	public JSONObject toJsonObject() {
		try {
			return new JSONObject(body);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public JSONArray toJsonArray() {
		try {
			return new JSONArray(body);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("success", success).append("body", body).toString();
	}


}
