package com.sendgrid.oai.go;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.constants.EnumConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.DefaultCodegen;

import java.io.File;


public class GoApiPackageGenerator extends ApiPackageGenerator {
    private final DefaultCodegen defaultCodegen;
    public GoApiPackageGenerator(DefaultCodegen defaultCodegen) {
        super(defaultCodegen);
        this.defaultCodegen = defaultCodegen;
    }

    @Override
    public void setOutputDir(final OpenAPI openAPI) {
        String domain = getDomain(openAPI).replaceAll(ApplicationConstants.HYPHEN_OR_UNDERSCORE, "");
        String version = getVersion(openAPI).replaceAll(ApplicationConstants.HYPHEN_OR_UNDERSCORE, "");
        String directory = getDirectory(openAPI);

        String apiPackage = EnumConstants.BasePackage.GO.getValue() + "." + domain + "." + version + "." + directory;
        String originalOutputDir = defaultCodegen.getOutputDir();
        defaultCodegen.setOutputDir(originalOutputDir + File.separator + apiPackage.replace(".", File.separator));
    }

    @Override
    public String getDomain(final OpenAPI openAPI) {
        String domain = super.getDomain(openAPI);
        return domain.replace("_", "").toLowerCase();
    }

    @Override
    public String getVersion(final OpenAPI openAPI) {
        String version = super.getVersion(openAPI);
        return version.replace("_", "").toLowerCase();
    }

    @Override
    public String getDirectory(final OpenAPI openAPI) {
        return super.getDirectory(openAPI);
    }
}
