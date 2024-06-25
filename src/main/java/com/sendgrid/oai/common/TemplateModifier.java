package com.sendgrid.oai.common;

import org.openapitools.codegen.DefaultCodegen;

public class TemplateModifier {
    private final DefaultCodegen defaultCodegen;

    public TemplateModifier(DefaultCodegen defaultCodegen) {
        this.defaultCodegen = defaultCodegen;
    }

    public void resetPredefinedTemplate() {
        defaultCodegen.additionalProperties().clear();
        defaultCodegen.supportingFiles().clear();
        defaultCodegen.apiTestTemplateFiles().clear();
        defaultCodegen.modelTestTemplateFiles().clear();
        defaultCodegen.apiDocTemplateFiles().clear();
        defaultCodegen.modelDocTemplateFiles().clear();
        defaultCodegen.supportingFiles().clear();
        defaultCodegen.setApiPackage("");
        defaultCodegen.setApiNameSuffix("");
        defaultCodegen.setEnsureUniqueParams(false);
        defaultCodegen.apiTemplateFiles().clear();
    }
    
    public void removeReservedWords() {
        
    }
    
    public void removeImportMapping() {
        
    }
}
