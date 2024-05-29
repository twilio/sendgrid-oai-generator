package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.constants.EnumConstants.QueryParams;
import com.sendgrid.oai.constants.EnumConstants.XOperation;
import com.sendgrid.oai.java.JavaGlobalCache;
import com.sendgrid.oai.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OperationProcessor {
    private CodegenOperation codegenOperation;
    JavaGlobalCache<String, Object> cache = JavaGlobalCache.getInstance();

    public void setCodegenOperation(CodegenOperation codegenOperation) {
        this.codegenOperation = codegenOperation;
    }

    public OperationProcessor pathParams() {
        if (codegenOperation.pathParams != null && !codegenOperation.pathParams.isEmpty()) {
            codegenOperation.vendorExtensions.put("has-pathParams", true);
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
            codegenOperation.vendorExtensions.put("has-queryParams", true);
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
            codegenOperation.vendorExtensions.put("has-headerParams", true);
        }
        return this;
    }

    public OperationProcessor body() {
        // test body with ref and w/o ref
        // test if properties are nested
        // test if properties are direct nested and ref nested
        if (codegenOperation.bodyParams != null && !codegenOperation.bodyParams.isEmpty()) {
            codegenOperation.vendorExtensions.put("has-body", true);
        }
        return this;
    }

    public OperationProcessor response() {
        setVariableNaming();
        return this;
    }

    public OperationProcessor operationId() {
        /*
         * Identify operation
         * 1. Combination of path and http method
         * 2. TODO: If there is any exception use operationId (identified by extension)
         */
        boolean isInstancePath = Utility.isInstancePath(codegenOperation.path);
        String httpMethod = codegenOperation.httpMethod;
        String operationType = null;
        switch (httpMethod.toUpperCase()) {
            case "GET":
                operationType = isInstancePath ? XOperation.FETCH.getValue() : XOperation.LIST.getValue();
                break;
            case "POST":
                operationType = isInstancePath ? XOperation.UPDATE.getValue() : XOperation.CREATE.getValue();
                break;
            case "PUT":
                operationType = isInstancePath ? XOperation.UPDATE.getValue() : null;
                break;
            case "DELETE":
                operationType = isInstancePath ? XOperation.DELETE.getValue() : null;
                break;
            case "PATCH":
                operationType = isInstancePath ? XOperation.PATCH.getValue() : null;
                break;
        }
        if (operationType != null) {
            codegenOperation.vendorExtensions.put(operationType, true);
        }
        return this;
    }

    private void setVariableNaming() {
        if (codegenOperation.responses == null || codegenOperation.responses.isEmpty()) return;
        // baseType in response should match in allModels key.
        
        for (CodegenResponse codegenResponse: codegenOperation.responses) {
            if (codegenResponse.is2xx) {
                codegenOperation.vendorExtensions.put("x-success-datatype", codegenResponse.dataType);
            }
            //getMappedResponse(codegenResponse);
        }
    }

    private void getMappedResponse(CodegenResponse codegenResponse) {
        Map<String, ModelsMap> allModels = (Map<String, ModelsMap>) cache.get("allModels");
        if (allModels.containsKey(codegenResponse.baseType)) {
            ModelsMap modelsMap = allModels.get(codegenResponse.baseType);
            List<ModelMap> modelMaps = modelsMap.getModels();
            codegenResponse.vendorExtensions.put("x-name", true);
        }
    }
}
