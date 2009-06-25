package com.googlecode.jplurk;

public class RequestParams {

	public final static String[] validParams = { "posted",
			"qualifier", "content", "lang", "no_comments", "uid", "limited_to" };

	private String endpoint;
	private String posted;
	private String qualifier;
	private String content;
	private String lang;
	private String no_comments;
	private String uid;
	private String limited_to;

	public void setPosted(String posted) {
		this.posted = posted;
	}

	public String getPosted() {
		return posted;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setNo_comments(String no_comments) {
		this.no_comments = no_comments;
	}

	public String getNo_comments() {
		return no_comments;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setLimited_to(String limited_to) {
		this.limited_to = limited_to;
	}

	public String getLimited_to() {
		return limited_to;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}
