package com.sendgrid.oai.java;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.common.TagGenerator;
import com.sendgrid.oai.common.TemplateModifier;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.constants.EnumConstants.MustacheLocation;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.List;
import java.util.Map;


public class SendgridJavaGenerator extends JavaClientCodegen {
    private final ApiPackageGenerator apiPackageGenerator;
    private final TagGenerator tagGenerator = new TagGenerator();
    private final TemplateModifier templateModifier;

    public SendgridJavaGenerator() {
        super();
        apiPackageGenerator = new JavApiPackageGenerator(this);
        templateModifier = new TemplateModifier(this);
    }

    @Override
    public void processOpts() {
        super.processOpts();
        sourceFolder = "";
        templateModifier.resetPredefinedTemplate();
        this.filesMetadataFilename = "";
        this.versionMetadataFilename = "";
        // Mustache file lookup location
        setTemplateDir(MustacheLocation.JAVA.getValue());
        setModelPackage("models");
        apiTemplateFiles().put("api.mustache", ".java");
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        apiPackageGenerator.setOutputDir(openAPI);
        
        tagGenerator.updateOperationTags(openAPI);
        this.openAPI = openAPI;
    }

//    public String toApiFilename(String name) {
//        return this.toApiName(name);
//    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        JavaApiResource javaApiResource = processCodegenOperations(results.getOperations().getOperation());
        results.put("resources", javaApiResource);
        return results;
    }

    // Operations are grouped based in tag applied to it in processOpenAPI() method.
    private JavaApiResource processCodegenOperations(final List<CodegenOperation> operations) {
        JavaOperationProcessor operationProcessor = new JavaOperationProcessor();
        JavaApiResourceBuilder javaApiResourceBuilder = new JavaApiResourceBuilder(operations, operationProcessor);
        javaApiResourceBuilder.process();
        return javaApiResourceBuilder.build();
    }

     //These models will be added by  DefaultGenerator.buildSupportFileBundle(...)
    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> allModels) {
        return super.postProcessAllModels(allModels);
    }

    @Override
    public String getName() {
        Integer i = 10;
        return EnumConstants.Generator.SENDGRID_JAVA.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the sendgrid-java helper library.";
    }
}
