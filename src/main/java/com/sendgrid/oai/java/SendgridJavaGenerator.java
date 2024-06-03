package com.sendgrid.oai.java;

import com.sendgrid.oai.common.DirectoryStructureService;
import com.sendgrid.oai.common.TwilioCodegenAdapter;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.modelprocessor.JavaModelProcessor;
import com.sendgrid.oai.resolver.JavaEnumResolver;
import com.sendgrid.oai.template.IApiActionTemplate;
import com.sendgrid.oai.template.JavaApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SendgridJavaGenerator extends JavaClientCodegen {

    JavaGlobalCache<String, Object> cache = JavaGlobalCache.getInstance();

    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties);
    private final TwilioCodegenAdapter twilioCodegen;

    private JavaEnumResolver javaEnumResolver = new JavaEnumResolver();
    private JavaCodegenResolver javaCodegenResolver = new JavaCodegenResolver(javaEnumResolver);
    private final IApiActionTemplate apiActionTemplate = new JavaApiActionTemplate(this);

    public SendgridJavaGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
        apiTemplateFiles().clear();
        apiTemplateFiles().put("api.mustache", ".java");
        apiTemplateFiles().put("creator.mustache", "Creator.java");
        apiTemplateFiles().put("model.mustache", "Model.java");
        //modelTemplateFiles().put("model.mustache", ".java");
        //apiTemplateFiles().put("model.mustache", ".java");
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
        //directoryStructureService.clearTag(openAPI);
        //directoryStructureService.configure(openAPI);
        this.openAPI = openAPI;
    }

    public String toApiFilename(String name) {
        return this.toApiName(name);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
//        ((OperationMap) results.get("operations")).put("classname", "SomeValue");
//        results.put("resources", "somaval");
        JavaApiResource javaApiResource = processCodegenOperations(results.getOperations().getOperation());
        
        results.put("resources", javaApiResource);
        return results;
    }

    public void generateModels(List<File> files, List<ModelMap> allModels, List<String> unusedModels) {
        System.out.println("Inside generate Models");
    }
    // Operations are grouped based in tag applied to it.
    private JavaApiResource processCodegenOperations(final List<CodegenOperation> operations) {
        System.out.println("---------- Operation List ------------");
        for (CodegenOperation operation: operations) {
            System.out.println(operation.path);
            System.out.println(operation.operationId);
        }
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
