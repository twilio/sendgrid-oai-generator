package com.sendgrid.oai.python;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.common.TagGenerator;
import com.sendgrid.oai.common.TemplateModifier;
import com.sendgrid.oai.constants.EnumConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
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
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        CodegenOperation codegenOperation = processCodegenOperations(results.getOperations().getOperation());
        results.put("resources", codegenOperation);
        return results;
    }

    private CodegenOperation processCodegenOperations(final List<CodegenOperation> operations) {
        PythonOperationProcessor operationProcessor = new PythonOperationProcessor();
        if (operations.isEmpty() || operations.size() > 1) {
            throw new RuntimeException("Grouping of operation not allowed, Remove similar tags from multiple operations");
        }
        
        CodegenOperation operation = operations.get(0);
        operationProcessor.setCodegenOperation(operation);
        
        return operation;
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
