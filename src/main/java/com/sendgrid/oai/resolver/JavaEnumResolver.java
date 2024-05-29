package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;


/*
 * Enum can be present in allModels, parameter(header, query, path)
 * 
 */
public class JavaEnumResolver implements IEnumResolver {

    EnumIdentifier enumIdentifier = new EnumIdentifier();
    @Override
    public void processEnum(CodegenProperty property) {
        if (enumIdentifier.isEnum(property)) {

        }
    }

    @Override
    public void processEnum(CodegenParameter parameter) {
        System.out.println("Parameter: " +parameter.baseName);
        if (enumIdentifier.isEnum(parameter)) {
            
        }
    }
}
