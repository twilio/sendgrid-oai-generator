package com.sendgrid.oai.utils;

public class Utility {
    public static boolean isInstancePath(String path) {
        String[] segments = path.split("/");
        if (segments.length == 0) {
            return false;
        }
        String lastSegment = segments[segments.length - 1];
        // Check if the last segment is enclosed in curly braces
        return lastSegment.matches("\\{[^/]+\\}");
    }
}
