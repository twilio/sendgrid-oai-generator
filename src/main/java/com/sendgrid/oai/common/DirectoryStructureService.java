package com.sendgrid.oai.common;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class DirectoryStructureService {

    @Getter
    private final Map<String, Object> additionalProperties;

    /*
     * Add tag for each path
     */
    public void configure(final OpenAPI openAPI) {

    }

    // Clear existing tags from open api
    public void clearTag(final OpenAPI openAPI) {
        Optional.ofNullable(openAPI.getPaths())
                .ifPresent(paths -> paths.forEach((path, pathItem) -> {
                    // Stream over all possible HTTP methods for a path
                    pathItem.readOperationsMap().values().stream()
                            .filter(operation -> operation != null)
                            .forEach(operation -> operation.setTags(Collections.emptyList()));
                }));
    }
}
