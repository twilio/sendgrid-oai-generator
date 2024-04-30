package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IEnumResolver {
    void processEnumProperty(CodegenProperty property);

    void processEnumParameter(CodegenParameter parameter);
}
