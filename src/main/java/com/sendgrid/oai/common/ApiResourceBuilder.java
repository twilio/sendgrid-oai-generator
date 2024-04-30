package com.sendgrid.oai.common;

import com.sendgrid.oai.template.ApiResource;

public class ApiResourceBuilder {
    protected ApiResource apiResource;
    protected OperationBuilder operationBuilder;

    public ApiResourceBuilder(ApiResource apiResource) {
        this.apiResource = apiResource;
    }

    public void process() {
        processServer();
        processApiPath();
        processOperations();
        processNestedModels();
    }

    public void processServer() {

    }

    public void processApiPath() {

    }

    public void processOperations() {

    }

    public void processNestedModels() {

    }
}
