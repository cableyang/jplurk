package com.google.jplurk;

public interface ISettings {

	public abstract String getApiKey();

	public abstract String getDefaultProxyHost();

	public abstract int getDefaultProxyPort();

	public abstract String getDefaultProxyUser();

	public abstract String getDefaultProxyPassword();

	public abstract MapHelper createParamMap();

	public abstract String getLang();

}