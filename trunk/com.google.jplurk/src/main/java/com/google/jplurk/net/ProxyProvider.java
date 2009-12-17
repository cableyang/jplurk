/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.jplurk.net;

/**
 *
 * @author askeing_yen
 */
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

    public static String getHost() {
        return ProxyProvider.host;
    }

    public static int getPort() {
        return ProxyProvider.port;
    }

    public static String getUser() {
        return ProxyProvider.user;
    }

    public static String getPassword() {
        return ProxyProvider.password;
    }

}
