package com.sendgrid.oai.java;

import com.sendgrid.oai.common.DirectoryStructureService;
import com.sendgrid.oai.common.TagGenerator;
import com.sendgrid.oai.common.TwilioCodegenAdapter;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.modelprocessor.JavaModelProcessor;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SendgridJavaGenerator extends JavaClientCodegen {

    JavaGlobalCache<String, Object> cache = JavaGlobalCache.getInstance();

    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties);
    private final TwilioCodegenAdapter twilioCodegen;

    private final TagGenerator tagGenerator = new TagGenerator();

    public SendgridJavaGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
        apiTemplateFiles().clear();
        apiTemplateFiles().put("api.mustache", ".java");
    }

    @Override
    public void processOpts() {
        super.processOpts();
        cache.clear();
        cache.putValue("initSchemas", new LinkedHashMap<>(openAPI.getComponents().getSchemas()));
        setTemplateDir("src/main/resources/sendgrid-java");
        setTemplateDir("sendgrid-java");
        setApiNameSuffix("");
        setApiPackage("");
        setEnsureUniqueParams(false);
        //supportingFiles().clear();
        twilioCodegen.processOpts();
        
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        additionalProperties.clear();
        supportingFiles.clear();

        additionalProperties().put("clientVersion", "v3");
        additionalProperties().put("apiVersion", "v3");
        additionalProperties().put("apiVersionClass", "v3");

        additionalProperties.put("apiFilename", "Shobhit");

        twilioCodegen.setDomain("api");
        twilioCodegen.setVersion("v3");
        twilioCodegen.setOutputDir("api", "v3");
        tagGenerator.updateOperationTags(openAPI);
        this.openAPI = openAPI;
    }

    public String toApiFilename(String name) {
        return this.toApiName(name);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        JavaApiResource javaApiResource = processCodegenOperations(results.getOperations().getOperation());
        results.put("resources", javaApiResource);
        return results;
    }

    // Operations are grouped based in tag applied to it in processOpenAPI() method.
    private JavaApiResource processCodegenOperations(final List<CodegenOperation> operations) {
        System.out.println("---------- Operation List ------------");
        JavaOperationProcessor operationProcessor = new JavaOperationProcessor();
        JavaApiResourceBuilder javaApiResourceBuilder= new JavaApiResourceBuilder(operations, operationProcessor);
        javaApiResourceBuilder.process();
       return javaApiResourceBuilder.build();
    }

    // These models will be added by  DefaultGenerator.buildSupportFileBundle(...)
    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> allModels) {
        JavaModelProcessor javaModelProcessor = new JavaModelProcessor(openAPI, allModels);
        javaModelProcessor.generateEnums(allModels);
        cache.put("allModels", allModels);
        return super.postProcessAllModels(allModels);
    }

    @Override
    public String modelPackage() {
        return "models";
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.SENDGRID_JAVA.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the sendgrid-java helper library.";
    }
}
