package com.google.jplurk;

public class Utils {

    public static String base36(long value) {
        return Long.toString(value, 36).toLowerCase();
    }

}
