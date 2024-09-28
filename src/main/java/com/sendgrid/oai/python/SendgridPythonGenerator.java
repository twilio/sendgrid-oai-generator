package com.sendgrid.oai.python;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.common.TagGenerator;
import com.sendgrid.oai.common.TemplateModifier;
import com.sendgrid.oai.constants.EnumConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.PythonClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.List;
import java.util.Map;

public class SendgridPythonGenerator extends PythonClientCodegen {
    private final ApiPackageGenerator apiPackageGenerator;
    private final TagGenerator tagGenerator = new TagGenerator();
    private final TemplateModifier templateModifier;
    public SendgridPythonGenerator() {
        super();
        apiPackageGenerator = new PythonApiPackageGenerator(this);
        templateModifier = new TemplateModifier(this);
    }

    @Override
    public void processOpts() {
        super.processOpts();
        templateModifier.resetPredefinedTemplate();
        this.filesMetadataFilename = "";
        this.versionMetadataFilename = "";
        // Mustache file lookup location
        setTemplateDir(EnumConstants.MustacheLocation.PYTHON.getValue());
        setModelPackage("models");
        apiTemplateFiles().put("api.mustache", ".py");
        
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        apiPackageGenerator.setOutputDir(openAPI);
        tagGenerator.updateOperationTags(openAPI);
        this.openAPI = openAPI;
        // Used for setting import base package.
        this.setPackageName(this.apiPackage);
        //String modelPackage = this.modelPackage.replace(".", "/");
        //String apiPackage = this.apiPackage.replace(".", "/");
        
        // __init__.mustache, __init__api.mustache, __init__model.mustache, __init__package.mustache
        // apiInfo ==> ApiInfoMap
        // apis ==> OperationsMap, org.openapitools.codegen.model
        this.supportingFiles.add(new SupportingFile("__init__model.mustache", modelPackage, "__init__.py"));
        this.supportingFiles.add(new SupportingFile("__init__api.mustache", apiPackage, "__init__.py"));
        //this.supportingFiles.add(new SupportingFile("__init__.mustache", "sendgrid/rest/api/alerts", "__init__.py"));
        //this.supportingFiles.add(new SupportingFile("__init__api.mustache", apiPackage, "__init__.py"));
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        PythonApiResource pythonApiResource = processCodegenOperations(results.getOperations().getOperation());
        results.put("resources", pythonApiResource);
        return results;
    }
    
    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        Map<String, Object> bundle = super.postProcessSupportingFileData(objs);
        bundle.put("api_version", "V3");
        return bundle;
    }

    private PythonApiResource processCodegenOperations(final List<CodegenOperation> operations) {
        if (operations.isEmpty() || operations.size() > 1) {
            throw new RuntimeException("Grouping of operation not allowed, Remove similar tags from multiple operations");
        }
        PythonOperationProcessor operationProcessor = new PythonOperationProcessor();
        PythonApiResourceBuilder pythonApiResourceBuilder = new PythonApiResourceBuilder(operations, operationProcessor);
        pythonApiResourceBuilder.process();
        return pythonApiResourceBuilder.build();
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> allModels) {
        return super.postProcessAllModels(allModels);
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.SENDGRID_PYTHON.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the sendgrid-python helper library.";
    }

}
