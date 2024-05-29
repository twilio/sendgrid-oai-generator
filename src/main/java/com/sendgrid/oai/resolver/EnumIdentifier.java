package com.sendgrid.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;


/*
 * Testing Results with Enums
 * ----------------------------------------
 *         Enum as Parameter
 * ----------------------------------------
 * 1. Not Container:
 *           property.isEnum = true, datatypeWithEnum = RegionEnum, dataType = string
 * 2. Container: 
 *          property.isEnum = true, property.items.isEnum = true, datatypeWithEnum = List<RegionEnum>, dataType = List<string>
 * 3. Ref not Container: 
 *          property.isEnumRef = true, datatypeWithEnum = null, dataType = RegionEnum
 * 4. Ref Container: 
 *          property.isEnumRef = false, property.items.isEnum = true, datatypeWithEnum = null, dataType = List<RegionEnum>
 * For all above 4 cases
 */
public class EnumIdentifier implements IEnumIdentifier {
    @Override
    public boolean isEnum(CodegenProperty property) {
        if (property.isEnum || property.isEnumRef) return true;
        if (property.isContainer) {
            return property.items != null && (property.items.isEnum || property.items.isEnumRef);
        }
        return false;
    }

    @Override
    public boolean isEnum(CodegenParameter parameter) {
        if (parameter.isEnum || parameter.isEnumRef) return true;
        if (parameter.isContainer) {
            return parameter.items != null && (parameter.items.isEnum || parameter.items.isEnumRef);
        }
        return false;
    }
}
