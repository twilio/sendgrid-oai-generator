package com.sendgrid.oai.common;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.DefaultCodegen;

import java.util.Arrays;

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
        return "v3";
    }

    public String getDomainFromOpenAPI(final OpenAPI openAPI) {
        return "abc";
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

    public void setOutputDir(final String domain, final String version) {
        final String domainPackage = domain.replaceAll("[-.]", "");
        final String versionPackage = version.replaceAll("[-.]", "");
        codegen.setOutputDir("codegen/abc/v3");
    }

}