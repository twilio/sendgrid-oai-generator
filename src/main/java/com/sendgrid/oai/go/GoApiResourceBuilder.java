package com.sendgrid.oai.go;

import com.sendgrid.oai.common.ApiResourceBuilder;
import com.sendgrid.oai.go.GoOperationProcessor;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class GoApiResourceBuilder extends ApiResourceBuilder {

    final List<CodegenOperation> operations;

    final GoOperationProcessor goOperationProcessor;
    public GoApiResourceBuilder(final List<CodegenOperation> operations, final GoOperationProcessor operationProcessor) {
        super(operations, operationProcessor);
        this.operations = operations;
        this.goOperationProcessor = operationProcessor;
    }

    @Override
    public GoApiResourceBuilder process() {
        processServer();
        processApiPath();
        processOperations();
        return this;
    }

    /*
     * Enums are build from allModels
     * Traverse allModels and find if there is any enum.
     * Then convert enum to modelMap as eventually enum need to be added to all models
     */

    @Override
    public GoApiResourceBuilder processServer() {
        super.processServer();
        return this;
    }

    @Override
    public GoApiResourceBuilder processApiPath() {
        super.processApiPath();
        return this;
    }

    @Override
    public GoApiResourceBuilder processOperations() {
        super.processOperations();
        return this;
    }

    @Override
    public GoApiResource build() {
        return new GoApiResource(this);
    }
}
