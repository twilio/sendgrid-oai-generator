package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.constants.EnumConstants.SupportedContentType;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenOperation;

import java.util.Map;

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
        if (codegenOperation.queryParams != null && !codegenOperation.queryParams.isEmpty()) {
            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_QUERY_PARAMS, true);
        }
        return this;
        /*
         * Sendgrid follows RQL for query parameters.
         * If query parameter is NonCompoundQueryParams it is passed as standard query parameter.
         * If query parameter is other than NonCompoundQueryParams then use 'query' field for query parameter
         * Customer will build compound query and pass it as a query parameter in query field.
         */
//        if (codegenOperation.queryParams != null && !codegenOperation.queryParams.isEmpty()) {
//            codegenOperation.vendorExtensions.put(ApplicationConstants.HAS_QUERY_PARAMS, true);
//            boolean isRemoved = codegenOperation.queryParams.removeIf(s -> !s.paramName.equals(QueryParams.OFFSET.getValue()) 
//                    && !s.paramName.equals(QueryParams.LIMIT.getValue()));
//            if (isRemoved) {
//                CodegenParameter query = new CodegenParameter();
//                query.paramName = QueryParams.QUERY.getValue();
//                query.baseName = QueryParams.QUERY.getValue();
//                query.dataType = EnumConstants.QueryDataType.JAVA.getValue();
//                codegenOperation.queryParams.add(query);
//            }
//        }
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
        return this;
    }
    
    public OperationProcessor fileName() {
        return this;
    }

    public OperationProcessor operationId() {
        return this;
    }

    public OperationProcessor contentType() {
        if (codegenOperation.consumes != null && !codegenOperation.consumes.isEmpty()) {
            for (Map<String, String> consume : codegenOperation.consumes) {
                String mediaType = consume.get("mediaType");
                if (mediaType != null) {
                    for (SupportedContentType contentType : SupportedContentType.values()) {
                        if (contentType.getValue().equals(mediaType)) {
                            codegenOperation.vendorExtensions.put("consume", contentType.getValue());
                            break;
                        }
                    }
                }
            }
        }
        return this;
    }


}
