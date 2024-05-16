package com.sendgrid.oai.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String SENDGRID_EXTENSION_NAME = "x-sendgrid";
    public static final String VERSION = "v3";
    public static String DOMAIN_REGEX = "^(?:https?://)?([^./]+)\\.";
    public static String FILE_SEPARATOR = "/";
}
