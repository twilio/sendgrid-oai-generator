package com.sendgrid.oai.java;

import com.sendgrid.oai.common.ApiResourceBuilder;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class JavaApiResourceBuilder extends ApiResourceBuilder {

    final List<CodegenOperation> operations;

    final JavaOperationProcessor javaOperationProcessor;
    public JavaApiResourceBuilder(final List<CodegenOperation> operations, final JavaOperationProcessor operationProcessor) {
        super(operations, operationProcessor);
        this.operations = operations;
        this.javaOperationProcessor = operationProcessor;
    }

    @Override
    public JavaApiResourceBuilder process() {
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

    public JavaApiResourceBuilder processServer() {
        super.processServer();
        return this;
    }

    public JavaApiResourceBuilder processApiPath() {
        super.processApiPath();
        return this;
    }

    public JavaApiResourceBuilder processOperations() {
        super.processOperations();
        return this;
    }

    public JavaApiResource build() {
        return new JavaApiResource(this);
    }
}
