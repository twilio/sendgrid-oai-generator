package com.sendgrid.oai.python;

import com.sendgrid.oai.common.OperationProcessor;
import org.openapitools.codegen.CodegenOperation;

public class PythonOperationProcessor extends OperationProcessor {
    private CodegenOperation codegenOperation;

    public void setCodegenOperation(CodegenOperation codegenOperation) {
        this.codegenOperation = codegenOperation;
        super.setCodegenOperation(codegenOperation);
    }

    @Override
    public OperationProcessor pathParams() {
        super.pathParams();
        return this;
    }
    @Override
    public OperationProcessor queryParams() {
        super.queryParams();
        return this;
    }

    @Override
    public OperationProcessor headerParams() {
        super.headerParams();
        return this;
    }
    public OperationProcessor body() {
        super.body();
        return this;
    }

    public OperationProcessor response() {
        super.response();
        //setResponseDataType();
        return this;
    }

    public OperationProcessor operationId() {
        super.operationId();
        return this;
    }
    
}
