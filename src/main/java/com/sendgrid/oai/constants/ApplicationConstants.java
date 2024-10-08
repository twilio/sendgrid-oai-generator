package com.sendgrid.oai.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String SENDGRID_EXTENSION_NAME = "x-sendgrid";
    public static final String LIBRARY_PACKAGE = "libraryPackage";
    public static final String INFO_LIBRARY_PACKAGE = "info" + "." + SENDGRID_EXTENSION_NAME + "." + LIBRARY_PACKAGE;
    public static final String HAS_PATH_PARAMS = "has-pathParams";
    public static final String HAS_HEADER_PARAMS = "has-headerParams";
    public static final String HAS_QUERY_PARAMS = "has-queryParams";
    public static final String HAS_BODY = "has-body";
    public static final String SUCCESS_DATATYPE = "x-success-datatype";
    public static final String SUCCESS_RETURN_TYPE = "x-success-return-type";
    public static final String ERROR_RETURN_TYPE = "x-error-return-type";
    public static final String SUCCESS_JSON_TYPE = "x-success-json-type";
    public static final String ERROR_JSON_TYPE = "x-error-json-type";
    public static final String VOID_DATATYPE = "x-void-datatype";
    public static final String SUCCESS_LIST_DATATYPE = "x-list-datatype";
    public static final String FAILURE_DATATYPE = "x-failure-datatype";
    public static final String IS_SUCCESS = "true";
    public static final String IS_FAILURE = "true";
    public static final String MOUNT_NAME = "mountName";
    public static final String API_NAME_SUFFIX = "_by_id";
    public static final String HYPHEN_OR_UNDERSCORE = "[-_]";
}
