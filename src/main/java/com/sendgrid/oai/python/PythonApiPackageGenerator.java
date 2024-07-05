package com.sendgrid.oai.python;

import com.sendgrid.oai.common.ApiPackageGenerator;
import com.sendgrid.oai.constants.EnumConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.DefaultCodegen;

public class PythonApiPackageGenerator extends ApiPackageGenerator  {
    private final DefaultCodegen defaultCodegen;
    public PythonApiPackageGenerator(DefaultCodegen defaultCodegen) {
        super(defaultCodegen);
        this.defaultCodegen = defaultCodegen;
    }

    @Override
    public void setOutputDir(final OpenAPI openAPI) {
        String domain = getDomain(openAPI);
        String version = getVersion(openAPI);
        String directory = getDirectory(openAPI);
        domain.replaceAll("[-._]", "");
        version.replaceAll("[-._]", "");
        directory.replaceAll("[-._]", "");

        String apiPackage = EnumConstants.BasePackage.PYTHON.getValue() + "." + domain + "." + version + "." +directory;
        //String apiPackage = domain + "." + version + "." +directory;
        defaultCodegen.setApiPackage(apiPackage);
        defaultCodegen.setModelPackage(apiPackage + "." + "models");
    }

    public String getDomain(final OpenAPI openAPI) {
        String domain = super.getDomain(openAPI);
        return domain.toLowerCase();
    }

    public String getVersion(final OpenAPI openAPI) {
        String version = super.getVersion(openAPI);
        return version.toLowerCase();
    }

    public String getDirectory(final OpenAPI openAPI) {
        String directory = super.getDirectory(openAPI);
        return directory.toLowerCase();
    }
}
