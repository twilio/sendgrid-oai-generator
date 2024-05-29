package com.sendgrid.oai.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String CREATE_OPERATION = "x-is-create-operation";
    public static final String LIST_OPERATION = "x-is-list-operation";
    public static final String SENDGRID_EXTENSION_NAME = "x-sendgrid";
}
