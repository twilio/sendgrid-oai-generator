package com.sendgrid.oai.python;

import com.sendgrid.oai.common.ApiResourceBuilder;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class PythonApiResourceBuilder extends ApiResourceBuilder {

    final List<CodegenOperation> operations;

    final PythonOperationProcessor pythonOperationProcessor;
    public PythonApiResourceBuilder(final List<CodegenOperation> operations, final PythonOperationProcessor operationProcessor) {
        super(operations, operationProcessor);
        this.operations = operations;
        this.pythonOperationProcessor = operationProcessor;
    }

    @Override
    public PythonApiResourceBuilder process() {
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

    public PythonApiResourceBuilder processServer() {
        super.processServer();
        return this;
    }

    public PythonApiResourceBuilder processApiPath() {
        super.processApiPath();
        return this;
    }

    public PythonApiResourceBuilder processOperations() {
        super.processOperations();
        return this;
    }

    public PythonApiResource build() {
        return new PythonApiResource(this);
    }
}
