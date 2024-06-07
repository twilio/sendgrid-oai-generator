package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.constants.EnumConstants.QueryParams;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenResponse;

@RequiredArgsConstructor
public class OperationProcessor {
    private CodegenOperation codegenOperation;

    public void setCodegenOperation(CodegenOperation codegenOperation) {
        this.codegenOperation = codegenOperation;
    }

    public OperationProcessor pathParams() {
        if (codegenOperation.pathParams != null && !codegenOperation.pathParams.isEmpty()) {
            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_PATH_PARAMS, true);
        }
        return this;
    }

    public OperationProcessor queryParams() {
        /*
         * Sendgrid follows RQL for query parameters.
         * If query parameter is limit or offset it is passed as standard query parameter.
         * If query parameter is other than limit or offset then use 'query' field for query parameter
         * Customer will build compound query, encode it and pass it as a query parameter in query field.
         */
        if (codegenOperation.queryParams != null && !codegenOperation.queryParams.isEmpty()) {
            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_QUERY_PARAMS, true);
            boolean isRemoved = codegenOperation.queryParams.removeIf(s -> !s.paramName.equals(QueryParams.OFFSET.getValue()) 
                    && !s.paramName.equals(QueryParams.LIMIT.getValue()));
            if (isRemoved) {
                CodegenParameter query = new CodegenParameter();
                query.paramName = QueryParams.QUERY.getValue();
                query.baseName = QueryParams.QUERY.getValue();
                query.dataType = EnumConstants.QueryDataType.JAVA.getValue();
                codegenOperation.queryParams.add(query);
            }
        }
        
        return this;
    }

    public OperationProcessor headerParams() {
        if (codegenOperation.headerParams != null && !codegenOperation.headerParams.isEmpty()) {
            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_HEADER_PARAMS, true);
        }
        return this;
    }

    public OperationProcessor body() {
        // test body with ref and w/o ref
        // test if properties are nested
        // test if properties are direct nested and ref nested
        if (codegenOperation.bodyParams != null && !codegenOperation.bodyParams.isEmpty()) {
            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_BODY, true);
        }
        return this;
    }

    public OperationProcessor response() {
        setResponseDataType();
        return this;
    }
    
    public OperationProcessor fileName() {
        return this;
    }

    public OperationProcessor operationId() {
        return this;
    }

    private void setResponseDataType() {
        if (codegenOperation.responses == null || codegenOperation.responses.isEmpty()) return;
        for (CodegenResponse codegenResponse: codegenOperation.responses) {
            if (codegenResponse.is2xx) {
                if (codegenResponse.dataType == null) {
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_DATATYPE, "void");
                    codegenOperation.vendorExtensions.put(ApplicationConstants.VOID_DATATYPE, true);
                } else {
                    codegenOperation.vendorExtensions.put(ApplicationConstants.SUCCESS_DATATYPE, codegenResponse.dataType);
                }
                
            }
        }
    }
}
