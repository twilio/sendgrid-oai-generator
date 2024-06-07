package com.sendgrid.oai.common;

import com.sendgrid.oai.constants.ApplicationConstants;
import com.sendgrid.oai.utils.StringHelper;
import com.sendgrid.oai.utils.Utility;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TagGenerator {
    public void updateOperationTags(final OpenAPI openAPI) {
        clearTag(openAPI);
        Paths paths = openAPI.getPaths();
        if (paths == null || paths.isEmpty()) {
            return;
        }
        // Process all paths
        for (Map.Entry<String, PathItem> pathEntry : paths.entrySet()) {
            String path = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            // Use snake casing
            String apiName = getMountName(pathItem);
            if (apiName == null) {
                apiName = extractLastSegmentOfPath(path);
            }
            String apiNameSuffix = Utility.isInstancePath(path) ? ApplicationConstants.API_NAME_SUFFIX : "";
            String apiNamePrefix = "";


            // Process all operations with a path, A path can have multiple operations
            if (pathItem.getGet() != null) {
                apiNamePrefix = "get_";
                addTagToOperation(pathItem.getGet(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getPost() != null) {
                apiNamePrefix = "post_";
                addTagToOperation(pathItem.getPost(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getPut() != null) {
                apiNamePrefix = "put_";
                addTagToOperation(pathItem.getPut(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getDelete() != null) {
                apiNamePrefix = "delete_";
                addTagToOperation(pathItem.getDelete(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getOptions() != null) {
                apiNamePrefix = "options_";
                addTagToOperation(pathItem.getOptions(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getHead() != null) {
                apiNamePrefix = "head_";
                addTagToOperation(pathItem.getHead(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getPatch() != null) {
                apiNamePrefix = "patch_";
                addTagToOperation(pathItem.getPatch(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
            if (pathItem.getTrace() != null) {
                apiNamePrefix = "trace_";
                addTagToOperation(pathItem.getTrace(), StringHelper.toSnakeCase(apiNamePrefix + apiName + apiNameSuffix));
            }
        }
    }

    public void clearTag(final OpenAPI openAPI) {
        Optional.ofNullable(openAPI.getPaths())
                .ifPresent(paths -> paths.forEach((path, pathItem) -> {
                    // Stream over all possible HTTP methods for a path
                    pathItem.readOperationsMap().values().stream()
                            .filter(operation -> operation != null)
                            .forEach(operation -> operation.setTags(Collections.emptyList()));
                }));
    }

    private String getMountName(PathItem pathItem) {
        if (pathItem.getExtensions() == null || pathItem.getExtensions().isEmpty()) return null;
        LinkedHashMap mount = (LinkedHashMap) pathItem.getExtensions().get(ApplicationConstants.SENDGRID_EXTENSION_NAME);
        return (String) mount.getOrDefault(ApplicationConstants.MOUNT_NAME, null);
    }

    private void addTagToOperation(Operation operation, String tag) {
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        operation.setTags(tags);
    }

    /*
     * path= /v1/student, output: student
     * path= /v1/student/{id}, output: student
     * path= /v1/student:batchAdd, output: student batchAdd
     * path= /v1/student/{id}:batchAdd, output: student batchAdd
     */
    private static String extractLastSegmentOfPath(final String path) {
        String[] colonSegments = path.split(":");
        String lastColonSegment = "";
        if (colonSegments.length > 1) {
            for (int i = colonSegments.length - 1; i >= 0; i--) {
                String segment = colonSegments[i];
                if (!segment.startsWith("{") && !segment.endsWith("}")) {
                    lastColonSegment = " " + segment;
                    break;
                }
            }
        }

        String basePath = colonSegments[0];
        String[] segments = basePath.split("/");
        String lastSegment = "";
        for (int i = segments.length - 1; i >= 0; i--) {
            String segment = segments[i];
            if (!segment.startsWith("{") && !segment.endsWith("}")) {
                lastSegment = segment;
                break;
            }
        }

        return lastSegment + lastColonSegment;
    }
}
