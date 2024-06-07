package com.sendgrid.oai.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {
    public static boolean isInstancePath(String path) {
        String[] basePath = path.split(":");
        
        String[] segments = basePath[0].split("/");
        if (segments.length == 0) {
            return false;
        }
        String lastSegment = segments[segments.length - 1];
        // Check if the last segment is enclosed in curly braces
        return lastSegment.matches("\\{[^/]+\\}");
    }
}
