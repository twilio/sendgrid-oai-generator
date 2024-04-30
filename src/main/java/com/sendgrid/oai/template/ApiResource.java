package com.sendgrid.oai.template;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;
import java.util.Set;

// Template for an open api spec operation group.
public class ApiResource {
    protected String server;

    protected String apiName;

    protected String apiPath;

    protected String recordKey;
    protected String version;

    protected String securitySchema;
    protected List<CodegenOperation> operations;

    protected Set<CodegenModel> nestedModels;

    protected boolean hasPagination;

}
