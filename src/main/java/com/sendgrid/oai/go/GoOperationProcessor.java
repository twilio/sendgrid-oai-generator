package com.sendgrid.oai.go;

import com.sendgrid.oai.common.OperationProcessor;
import com.sendgrid.oai.constants.ApplicationConstants;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenResponse;

public class GoOperationProcessor extends OperationProcessor {
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
        setResponseDataType();
        return this;
    }

    public OperationProcessor operationId() {
        super.operationId();
        return this;
    }

    /*
     * Return type needs to be set at 2 places.
     * 1. While passing in JsonUtil
     * 2. In Return type.
     */
    private void setResponseDataType() {
        if (codegenOperation.responses == null || codegenOperation.responses.isEmpty()) return;
        for (CodegenResponse codegenResponse: codegenOperation.responses) {
            if (codegenResponse.is2xx) {
                if (codegenResponse.dataType == null) {
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_RETURN_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_JSON_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.VOID_DATATYPE, true);
                } else {
                    setSuccessDatatype(codegenResponse);
                }
            }
            // Set Redirection as Success only when 2xx response not present.
             else if (codegenResponse.is3xx) {
                if (codegenResponse.dataType == null) {
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_RETURN_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_JSON_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.VOID_DATATYPE, true);
                } else {
                    setSuccessDatatype(codegenResponse);
                }
            }
             // Set Error respponse
             else {
                if (codegenResponse.dataType == null) {
                    codegenOperation.vendorExtensions.put(ApplicationConstants.ERROR_RETURN_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.ERROR_JSON_TYPE, "Void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.VOID_DATATYPE, true);
                } else {
                    setErrorDatatype(codegenResponse);
                }
            }
        }
    }

    public void setSuccessDatatype(CodegenResponse codegenResponse) {
        String successJsontype;
        if (codegenResponse.containerTypeMapped != null) {
            successJsontype = codegenResponse.containerTypeMapped;
            codegenResponse.vendorExtensions.put(ApplicationConstants.SUCCESS_LIST_DATATYPE, true);
        } else {
            successJsontype = codegenResponse.dataType;
        }
        codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_JSON_TYPE, successJsontype);
        codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_RETURN_TYPE, codegenResponse.dataType);
    }

    public void setErrorDatatype(CodegenResponse codegenResponse) {
        String errorJsontype;
        if (codegenResponse.containerTypeMapped != null) {
            errorJsontype = codegenResponse.containerTypeMapped;
            codegenResponse.vendorExtensions.put(ApplicationConstants.SUCCESS_LIST_DATATYPE, true);
        } else {
            errorJsontype = codegenResponse.dataType;
        }
        codegenResponse.vendorExtensions.put(ApplicationConstants.ERROR_JSON_TYPE, errorJsontype);
        codegenResponse.vendorExtensions.put(ApplicationConstants.ERROR_RETURN_TYPE, codegenResponse.dataType);
    }

}
