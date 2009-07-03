package com.googlecode.jplurk.net;

import java.util.HashMap;
import java.util.Map;

public class Result {

	public final static Result FAILURE_RESULT = new Result() {
		{
			setOk(false);
		}
	};

	private boolean ok;
	private String responseBody;

	@SuppressWarnings("unchecked")
	private Map attachment = new HashMap();

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return String.format("isOk: %s, attachment: %s\nresponse: %s", isOk(), attachment, responseBody);
	}

	@SuppressWarnings("unchecked")
	public Map getAttachement(){
		return attachment;
	}

}
