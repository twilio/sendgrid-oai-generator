package com.sendgrid.oai.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String CREATE_OPERATION = "x-is-create-operation";
    public static final String LIST_OPERATION = "x-is-list-operation";
    public static final String SENDGRID_EXTENSION_NAME = "x-sendgrid";
    public static final String HAS_PATH_PARAMS = "has-pathParams";
    public static final String HAS_HEADER_PARAMS = "has-headerParams";
    public static final String HAS_QUERY_PARAMS = "has-queryParams";
    public static final String HAS_BODY = "has-body";
    public static final String SUCCESS_DATATYPE = "x-success-datatype";
    public static final String FAILURE_DATATYPE = "x-success-datatype";
    public static final String IS_SUCCESS = "true";
    public static final String IS_FAILURE = "true";
}
