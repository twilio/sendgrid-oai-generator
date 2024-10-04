package com.sendgrid.oai.common;

import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

@RequiredArgsConstructor
public class ApiResourceBuilder {
    final List<CodegenOperation> operations;
    final OperationProcessor operationProcessor;

    public ApiResourceBuilder process() {
        processServer();
        processApiPath();
        processOperations();
        return this;
    }

    public ApiResourceBuilder processServer() {
        return this;
    }

    public ApiResourceBuilder processApiPath() {
        return this;
    }

    public ApiResourceBuilder processOperations() {
        for (CodegenOperation operation: operations) {
            operationProcessor.setCodegenOperation(operation);
            operationProcessor
                    .operationId()
                    .pathParams()
                    .queryParams()
                    .headerParams()
                    .body()
                    .response()
                    .contentType();
        }
        return this;
    }

    public ApiResource build() {
        return new ApiResource(this);
    }
}
