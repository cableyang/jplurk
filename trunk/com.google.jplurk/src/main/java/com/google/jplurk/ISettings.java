package com.google.jplurk;

import java.util.HashMap;

public interface ISettings {

	public abstract String getApiKey();

	public abstract String getDefaultProxyHost();

	public abstract int getDefaultProxyPort();

	public abstract String getDefaultProxyUser();

	public abstract String getDefaultProxyPassword();

	public abstract MapHelper createParamMap();

	public abstract String getLang();

	static class Simple implements ISettings {

		private String apiKey;
		private Lang lang;

		public Simple(String apiKey, Lang lang) {
			this.apiKey = apiKey;
			this.lang = (lang == null ? Lang.en : lang);
		}

		public MapHelper createParamMap() {
			MapHelper m = new MapHelper(new HashMap<String, String>());
			m.k("api_key").v(getApiKey());
			return m;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getDefaultProxyHost() {
			return null;
		}

		public String getDefaultProxyPassword() {
			return null;
		}

		public int getDefaultProxyPort() {
			return 0;
		}

		public String getDefaultProxyUser() {
			return null;
		}

		public String getLang() {
			return lang.toString();
		}
	}

}