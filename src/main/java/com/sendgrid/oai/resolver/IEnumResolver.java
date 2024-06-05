package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IEnumResolver {
    void processEnum(CodegenProperty property);

    void processEnum(CodegenParameter parameter);
}
