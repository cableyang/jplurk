package com.googlecode.jplurk.net;

public class ProxyProvider {

	static String host;
	static int port = 80;
	static String user;
	static String password;

	public static void setProvider(String host, int port) {
		setProvider(host);
		ProxyProvider.port = port;
	}

	public static void setProvider(String host) {
		ProxyProvider.host = host;
	}

	public static void setAuthInfo(String user, String password) {
		ProxyProvider.user = user;
		ProxyProvider.password = password;
	}

}
