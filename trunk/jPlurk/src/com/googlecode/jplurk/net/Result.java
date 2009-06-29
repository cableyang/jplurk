package com.googlecode.jplurk.net;

public class Result {

	public final static Result FAILURE_RESULT = new Result() {
		{
			setOk(false);
		}
	};

	private boolean ok;
	private String responseBody;

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
		return String.format("isOk: %s, response: %s", isOk(), responseBody);
	}

}
