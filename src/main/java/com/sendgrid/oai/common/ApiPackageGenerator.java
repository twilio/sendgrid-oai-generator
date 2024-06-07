package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.utils.StringHelper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;
import org.openapitools.codegen.DefaultCodegen;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Stream;


public abstract class ApiPackageGenerator {
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
        defaultCodegen.getInputSpec();
        String version = defaultCodegen.getInputSpec().replaceAll(INPUT_SPEC_PATTERN, "${version}");
        return StringHelper.toSnakeCase(version);
    }

    // Return library directory in snake_case
    public String getDirectory(final OpenAPI openAPI) {
        if (openAPI.getInfo().getExtensions() == null) {
            throw new RuntimeException(ApplicationConstants.LIBRARY_DIRECTORY + " is required");
        }
        String directory = (String)((LinkedHashMap)openAPI.getInfo().getExtensions()
                .get(ApplicationConstants.SENDGRID_EXTENSION_NAME))
                .get(ApplicationConstants.LIBRARY_DIRECTORY);
        return StringHelper.toSnakeCase(directory);
    }

    public abstract void setOutputDir(final OpenAPI openAPI);
}