package com.sendgrid.oai.go;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.common.TagGenerator;
import com.sendgrid.oai.common.TemplateModifier;
import com.sendgrid.oai.constants.EnumConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.GoClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SendgridGoGenerator extends GoClientCodegen {
    private final ApiPackageGenerator apiPackageGenerator;
    private final TagGenerator tagGenerator = new TagGenerator();
    private final TemplateModifier templateModifier;

    public SendgridGoGenerator() {
        super();
        apiPackageGenerator = new GoApiPackageGenerator(this);
        templateModifier = new TemplateModifier(this);
    }

    @Override
    public void processOpts() {
//        additionalProperties.put("modelFileFolder", "models"); // sets models folder for model files
        super.processOpts();
        templateModifier.resetPredefinedTemplate();
        this.filesMetadataFilename = "";
        this.versionMetadataFilename = "";
        // Mustache file lookup location
        setTemplateDir(EnumConstants.MustacheLocation.GO.getValue());
        apiTemplateFiles().put("api.mustache", ".go");
        apiPackageGenerator.setOutputDir(openAPI); // appends domain, version and directory to current output directory
        supportingFiles.add(new SupportingFile("api_service.mustache", "api_service.go"));
        supportingFiles.add(new SupportingFile("README.mustache", "README.md"));
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        tagGenerator.updateOperationTags(openAPI);
        this.openAPI = openAPI;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        List<CodegenOperation> operations = results.getOperations().getOperation();
        processCodegenOperations(operations);
        return results;
    }

    private void processCodegenOperations(final List<CodegenOperation> operations) {
//        GoOperationProcessor operationProcessor = new GoOperationProcessor();
//        GoApiResourceBuilder goApiResourceBuilder = new GoApiResourceBuilder(operations, operationProcessor);
//        goApiResourceBuilder.process();
//        return goApiResourceBuilder.build();
        processServer();
        processApiPath();
        processOperation(operations);
    }

    private void processServer() {
        // TODO add server processing here
    }

    private void processApiPath() {
        // TODO add api path processing here
    }

    private void processOperation(final List<CodegenOperation> operations) {
        GoOperationProcessor operationProcessor = new GoOperationProcessor();
        for (CodegenOperation operation: operations) {
            operationProcessor.setCodegenOperation(operation);
            operationProcessor
                    .operationId()
                    .pathParams()
                    .queryParams()
                    .headerParams()
                    .body()
                    .response();
        }
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.SENDGRID_GO.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the sendgrid-go helper library.";
    }

}
