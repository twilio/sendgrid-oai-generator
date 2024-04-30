package com.sendgrid.oai.common;

import org.openapitools.codegen.CodegenOperation;

import java.util.List;

// Template for an open api spec operation group.
public class ApiResource {
    protected String server;
    protected String apiName;
    protected String apiPath;
    protected String recordKey;
    protected String version;
    protected String securitySchema;
    protected List<CodegenOperation> operations;
    protected boolean hasPagination;

}
