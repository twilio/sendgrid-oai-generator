package com.sendgrid.oai.adaptor;

import org.openapitools.codegen.model.ModelMap;


/**
 * The Adapter is used to create a ModelMap object.
 * Enums can be present as CodegenParameter or CodegenProperty.
 * To add these to allModels, they need to be converted to ModelMap.
 */
public interface ModelMapAdapter {
    ModelMap convertToModelMap();
}
