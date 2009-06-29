package com.googlecode.jplurk.net;

import java.util.HashMap;
import java.util.Map;

public class Request {

	String endPoint;

	String userUId;

	Map<String, String> params = new HashMap<String, String>();

	public String getUserUId() {
		return userUId;
	}

	public void setUserUId(String userId) {
		this.userUId = userId;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public void addParam(String key, String value) {
		params.put(key, value);
	}

}
