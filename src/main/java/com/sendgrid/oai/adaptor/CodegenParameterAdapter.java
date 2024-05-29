package com.sendgrid.oai.adaptor;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.model.ModelMap;

public class CodegenParameterAdapter implements ModelMapAdapter {
    private final CodegenParameter codegenParameter;

    public CodegenParameterAdapter(CodegenParameter codegenParameter) {
        this.codegenParameter = codegenParameter;
    }
    @Override
    public ModelMap convertToModelMap() {
        ModelMap modelMap = new ModelMap();
        return modelMap;
    }
}
