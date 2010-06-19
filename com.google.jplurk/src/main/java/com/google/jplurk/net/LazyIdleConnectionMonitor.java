package com.google.jplurk.net;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;

public class LazyIdleConnectionMonitor {
    
    public static void cleanIdleConnections(
            ClientConnectionManager connectionManager) {
        try {
            // Close expired connections
            connectionManager.closeExpiredConnections();
            
            // Optionally, close connections
            // that have been idle longer than 30 sec
            connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }
    }

}
