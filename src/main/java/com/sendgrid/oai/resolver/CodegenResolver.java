package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class CodegenResolver {
    protected IEnumResolver IEnumResolver;

    public CodegenResolver(final IEnumResolver IEnumResolver) {
        this.IEnumResolver = IEnumResolver;
    }

    public void processModel(final CodegenModel model) {
        System.out.println("Processing model: " + model.name);

        if (model.vars != null) {
            for (CodegenProperty property : model.vars) {
                processProperty(property);
            }
        }
    }

    public void processParameter(final CodegenParameter parameter) {
        System.out.println("Processing parameter: " + parameter.baseName);
        if (parameter.isEnum) {
            IEnumResolver.processEnumParameter(parameter);
        } else {
            if (parameter.vars != null) {
                for (CodegenProperty property : parameter.vars) {
                    processProperty(property);
                }
            }
        }
        
    }

    public void processProperty(final CodegenProperty property) {
        System.out.println("Processing property: " + property.baseName + ", Type: " + property.dataType);

        if (property.isEnum) {
            IEnumResolver.processEnumProperty(property);
        } else {
            if (property.vars != null) {
                for (CodegenProperty nestedProperty : property.vars) {
                    processProperty(nestedProperty);
                }
            }
        }
    }
}
