package com.sendgrid.oai.template;

import org.openapitools.codegen.CodegenConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaApiActionTemplate extends AbstractApiActionTemplate {
    public JavaApiActionTemplate(CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    public Map<String, List<String>> mapping() {
        return Map.of("create",
                Arrays.asList("creator.mustache", "Creator.java"),
                "fetch",
                Arrays.asList("fetcher.mustache", "Fetcher.java"),
                "delete",
                Arrays.asList("deleter.mustache", "Deleter.java"),
                "read",
                Arrays.asList("reader.mustache", "Reader.java"),
                "update",
                Arrays.asList("patcher.mustache", "Reader.java"),
                "patch",
                Arrays.asList("updater.mustache", "Updater.java"),
                API_TEMPLATE,
                Arrays.asList("api.mustache", ".java"),
                MODELS,
                Arrays.asList("model.mustache", "Model.java")
        );
    }
}
