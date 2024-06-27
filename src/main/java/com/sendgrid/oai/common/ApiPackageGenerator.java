package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.utils.StringHelper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.openapitools.codegen.DefaultCodegen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ApiPackageGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ApiPackageGenerator.class);
    private static final String INPUT_SPEC_PATTERN = "[^_]+_(?<domain>.+?)(_(?<version>[^_]+))?\\..+";
    private static final String DEFAULT_URL = "/";
    private static final String SERVER_PATTERN = "https://(?<domain>[^:/?\\n]+)\\.sendgrid\\.com\\/?";
    DefaultCodegen defaultCodegen;
    public ApiPackageGenerator(DefaultCodegen defaultCodegen) {
        this.defaultCodegen = defaultCodegen;
    }

    public String getDomain(final OpenAPI openAPI) {
        String serverDomain = "";
        if (openAPI.getServers() != null) {
            Optional<String> url = openAPI.getServers().stream().findFirst().map(Server::getUrl);
            if (url.isPresent() && !url.get().equals(DEFAULT_URL)){
                serverDomain =  url.get().replaceAll(SERVER_PATTERN, "${domain}");
                return StringHelper.toSnakeCase(serverDomain);
            }
        }

        serverDomain = openAPI
                .getPaths()
                .values()
                .stream()
                .findFirst()
                .map(PathItem::getServers)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(Server::getUrl)
                .map(url -> url.replaceAll(SERVER_PATTERN, "${domain}"))
                .orElseThrow();
        return StringHelper.toSnakeCase(serverDomain);
    }

    public String getVersion(final OpenAPI openAPI) {
        String fileName = defaultCodegen.getInputSpec();
        logger.debug("Processing File: ",fileName);
        
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex);
        }
        int underscoreIndex = fileName.lastIndexOf('_');
        if (underscoreIndex != -1) {
            return StringHelper.toSnakeCase(fileName.substring(underscoreIndex + 1));
        }
        throw new RuntimeException(fileName + " open api spec file name has invalid version");
    }

    // Return library directory in snake_case
    public String getDirectory(final OpenAPI openAPI) {
        Info info = openAPI.getInfo();
        if (info == null || info.getExtensions() == null
                || info.getExtensions().get(ApplicationConstants.SENDGRID_EXTENSION_NAME) == null) {
            throw new RuntimeException(ApplicationConstants.INFO_LIBRARY_PACKAGE + " is required in OpenAPI specification");
        }
        
        LinkedHashMap<String, String> sendgridExtension = (LinkedHashMap)info.getExtensions().get(ApplicationConstants.SENDGRID_EXTENSION_NAME);
        if (sendgridExtension.get(ApplicationConstants.LIBRARY_PACKAGE) == null) {
            throw new RuntimeException(ApplicationConstants.INFO_LIBRARY_PACKAGE + " is required in OpenAPI specification");
        }
        
        String directory = sendgridExtension.get(ApplicationConstants.LIBRARY_PACKAGE);
        return StringHelper.toSnakeCase(directory);
    }

    public abstract void setOutputDir(final OpenAPI openAPI);
}
