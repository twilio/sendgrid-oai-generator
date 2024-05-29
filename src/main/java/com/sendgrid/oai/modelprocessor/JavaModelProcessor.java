package com.sendgrid.oai.modelprocessor;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.model.ModelsMap;

import java.util.Map;

public class JavaModelProcessor {

    Map<String, ModelsMap> allModels;
    

    public JavaModelProcessor(OpenAPI openAPI, Map<String, ModelsMap> allModels) {
        this.allModels = allModels;
    }
    public Map<String, ModelsMap> process() {
        processClassName();
        processDataType();
        return allModels;
    }

    public void generateEnums(final Map<String, ModelsMap> allModels) {
        
    }

    private void processFileName(Map<String, ModelsMap> tempModels) {
        
    }
    private void processDataType() {
    }

    private void processClassName() {
    }
    
    private void processEnum() {
        
    }


}
