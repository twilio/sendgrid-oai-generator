package com.sendgrid.oai.java;

import com.sendgrid.oai.common.DirectoryStructureService;
import com.sendgrid.oai.common.TwilioCodegenAdapter;
import com.sendgrid.oai.constants.EnumConstants;
import com.sendgrid.oai.template.IApiActionTemplate;
import com.sendgrid.oai.template.JavaApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.List;
import java.util.Map;

public class SendgridJavaGenerator extends JavaClientCodegen {

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
        //modelTemplateFiles().put("model.mustache", ".java");
        //apiTemplateFiles().put("model.mustache", ".java");
    }

    @Override
    public void processOpts() {
        super.processOpts();
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
        //additionalProperties.clear();
        supportingFiles.clear();

        additionalProperties().put("clientVersion", "v3");
        additionalProperties().put("apiVersion", "v3");
        additionalProperties().put("apiVersionClass", "v3");

        additionalProperties.put("apiFilename", "Shobhit");
        String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        String version = twilioCodegen.getVersionFromOpenAPI(openAPI);
        String extension = twilioCodegen.getExtensionFromOpenAPI(openAPI,additionalProperties.get("inputSpec").toString());
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version,extension);
        //directoryStructureService.clearTag(openAPI);
        //directoryStructureService.configure(openAPI);
    }


    public String toApiFilename(String name) {
        return this.toApiName(name);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        ((OperationMap) results.get("operations")).put("classname", "SomeValue");
        results.put("resources", "somaval");
        return results;
    }

    // These models will be added by  DefaultGenerator.buildSupportFileBundle(...)
    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {

        for (Map.Entry<String, ModelsMap> entry : allModels.entrySet()) {
            ModelsMap modelsMap = entry.getValue();
            List<ModelMap> modelMaps = modelsMap.getModels();
            for (ModelMap modelMap : modelMaps) {
                CodegenModel codegenModel = modelMap.getModel();
                //System.out.println(codegenModel);
            }
        }
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
