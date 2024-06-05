package com.sendgrid.oai.adaptor;

import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.model.ModelMap;

public class CodegenPropertyAdapter implements ModelMapAdapter {

    private final CodegenProperty codegenProperty;

    public CodegenPropertyAdapter(CodegenProperty codegenProperty) {
        this.codegenProperty = codegenProperty;
    }
    @Override
    public ModelMap convertToModelMap() {
        ModelMap modelMap = new ModelMap();
        return modelMap;
    }
}
