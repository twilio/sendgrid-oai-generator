package com.sendgrid.oai.java;

import com.sendgrid.oai.resolver.CodegenResolver;
import com.sendgrid.oai.resolver.IEnumResolver;
import com.sendgrid.oai.resolver.JavaEnumResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class JavaCodegenResolver extends CodegenResolver {
    JavaEnumResolver javaEnumResolver = new JavaEnumResolver();
    public JavaCodegenResolver(IEnumResolver iEnumResolver) {
        super(iEnumResolver);
    }

    @Override
    public void processModel(CodegenModel model) {
        super.processModel(model);
        
    }

    @Override
    public void processParameter(CodegenParameter parameter) {
        super.processParameter(parameter);
    }

    @Override
    public void processProperty(CodegenProperty property) {
        super.processProperty(property);
    }
}
