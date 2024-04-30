package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IEnumIdentifier {
    boolean isEnum(CodegenProperty property);

    boolean isEnum(CodegenParameter parameter);
}
