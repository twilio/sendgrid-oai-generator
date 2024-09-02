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
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        processServer();
        processApiPath(operations);
        processOperation(operations);
    }

    private void processServer() {
        // TODO add server processing here
    }

    private void processApiPath(final List<CodegenOperation> operations) {
        camelizePathParams(operations);
    }

    private void processOperation(final List<CodegenOperation> operations) {
        GoOperationProcessor operationProcessor = new GoOperationProcessor();
        for (CodegenOperation operation: operations) {
            operationProcessor.setCodegenOperation(operation);
            operationProcessor
                    .operationId()
                    .params()
                    .response();
        }
    }

    private void camelizePathParams(final List<CodegenOperation> operations) {
        // Regular pattern to match path parameters
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        for(CodegenOperation operation: operations) {
            Matcher matcher = pattern.matcher(operation.path);
            while (matcher.find()) {
                String pathParam = matcher.group(1);
                operation.path = operation.path.replace("{" + pathParam + "}", "{" + StringUtils.camelize(pathParam) + "}");
            }
        }
    }

    @Override
    public String toParamName(String name) {
        name = name.replaceAll("[-+.^:,]", "");
        name = name.replace("<", "Before").replace(">", "After");
        return super.toVarName(name);
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
