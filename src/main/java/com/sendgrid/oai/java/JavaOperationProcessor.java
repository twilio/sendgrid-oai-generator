package com.sendgrid.oai.java;

import com.sendgrid.oai.common.OperationProcessor;
import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.resolver.JavaEnumResolver;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenResponse;

import java.util.stream.Collectors;

public class JavaOperationProcessor extends OperationProcessor {
    private CodegenOperation codegenOperation;
    
    JavaEnumResolver javaEnumResolver = new JavaEnumResolver();

    public void setCodegenOperation(CodegenOperation codegenOperation) {
        this.codegenOperation = codegenOperation;
        super.setCodegenOperation(codegenOperation);
    }

    @Override
    public OperationProcessor pathParams() {
        super.pathParams();
        codegenOperation.pathParams = codegenOperation.pathParams.stream()
                .map(param -> {
                    javaEnumResolver.processEnum(param);
                    return param;
                })
                .collect(Collectors.toList());
        return this;
    }
    @Override
    public OperationProcessor queryParams() {
        super.queryParams();
        codegenOperation.queryParams = codegenOperation.queryParams.stream()
                .map(param -> {
                    if (EnumConstants.QueryParams.QUERY.getValue().equals(param.paramName)) {
                        param.dataType = EnumConstants.QueryDataType.JAVA.getValue();
                    }
                    javaEnumResolver.processEnum(param);
                    return param;
                })
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public OperationProcessor headerParams() {
        super.headerParams();
        codegenOperation.headerParams = codegenOperation.headerParams.stream()
                .map(param -> {
                    javaEnumResolver.processEnum(param);
                    return param;
                })
                .collect(Collectors.toList());
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
        }
    }
    
    public void setSuccessDatatype(CodegenResponse codegenResponse) {
        String successJsontype = "Void";
        if (codegenResponse.containerTypeMapped != null) {
            successJsontype = codegenResponse.containerTypeMapped;
            codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_LIST_DATATYPE, true);
        } else {
            successJsontype = codegenResponse.dataType;
        }
        codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_JSON_TYPE, successJsontype);
        codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_RETURN_TYPE, codegenResponse.dataType);
        
    }
    
}
