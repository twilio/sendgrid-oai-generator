package com.sendgrid.oai.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.DefaultCodegen;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.sendgrid.oai.constants.ApplicationConstants.*;

@RequiredArgsConstructor
public class TwilioCodegenAdapter {

    private final DefaultCodegen codegen;
    private final String name;

    private String originalOutputDir;

    public void processOpts() {
        // Find the templates in the local resources dir.
        codegen.setTemplateDir(name);
        // Remove the "API" suffix from the API filenames.
        codegen.setApiNameSuffix("");
        codegen.setApiPackage("");
        codegen.setEnsureUniqueParams(false);

        originalOutputDir = codegen.getOutputDir();
        setDomain("api");
        final String version = "v3";
        codegen.additionalProperties().put("clientVersion", version);
        codegen.additionalProperties().put("apiVersion", version);
        codegen.additionalProperties().put("apiVersionClass", "V3");
        codegen.supportingFiles().clear();

        Arrays.asList("Configuration", "Parameter", "Version").forEach(word -> {
            codegen.reservedWords().remove(word);
            codegen.reservedWords().remove(word.toLowerCase());
        });

    }

    public String getVersionFromOpenAPI(final OpenAPI openAPI) {
        return openAPI.getInfo().getVersion() == null ? VERSION : openAPI.getInfo().getVersion();
    }

    public String getDomainFromOpenAPI(final OpenAPI openAPI) {
        Server server = openAPI.getServers().get(0);
        Pattern pattern = Pattern.compile(DOMAIN_REGEX);
        Matcher matcher = pattern.matcher(server.getUrl());
        matcher.find();
        return matcher.group(1);
    }

    public String getExtensionFromOpenAPI(OpenAPI openAPI,String inputSpec){
        return openAPI.getInfo().getExtensions() != null ? getExtensionFromOpenAPISpec(openAPI.getInfo().getExtensions()) : getExtensionFromFileName(inputSpec);
    }

    public String getExtensionFromOpenAPISpec(Map<String,Object> extensions){
        String extensionValue = extensions.get("x-extension").toString();
        String directoryValue = extensionValue.split("_")[0] + Arrays.stream(extensionValue.split("_"), 1, extensionValue.split("_").length).map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining());
        return directoryValue;

    }

    public String getExtensionFromFileName(String inputSpec){
        String[] inputSpecArray = inputSpec.split("//");
        String fileName = inputSpecArray[inputSpecArray.length-1];
        fileName = fileName.substring(0,fileName.indexOf(".yaml"));
        return fileName.split("_")[1] + Arrays.stream(fileName.split("_"), 2, fileName.split("_").length).map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining());
    }

    public void setDomain(final String domain) {
        final String domainPackage = domain.replaceAll("[-.]", "");

        codegen.additionalProperties().put("domainName", domain);
        codegen.additionalProperties().put("domainPackage", domainPackage);
    }

    public void setVersion(final String version) {
        codegen.additionalProperties().put("clientVersion", version);
        codegen.additionalProperties().put("apiVersion", version);
        codegen.additionalProperties().put("apiVersionClass", "V3");
    }

    public void setOutputDir(final String domain, final String version, final String extension) {
        final String domainPackage = domain.replaceAll("[-.]", "");
        final String versionPackage = version.replaceAll("[-.]", "");
        codegen.setOutputDir(originalOutputDir + FILE_SEPARATOR + domainPackage + FILE_SEPARATOR + extension + FILE_SEPARATOR + versionPackage);
    }

}
