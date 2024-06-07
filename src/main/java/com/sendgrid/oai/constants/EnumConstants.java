package com.sendgrid.oai.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EnumConstants {

    @Getter
    @RequiredArgsConstructor
    public enum Generator {
        SENDGRID_JAVA("sendgrid-java"),
        JAVA("java");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        CREATE("create"),
        LIST("list"),
        UPDATE("update"),
        DELETE("delete"),
        FETCH("fetch"),
        PATCH("patch");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum XOperation {
        CREATE("x-create-operation"),
        LIST("x-list-operation"),
        UPDATE("x-update-operation"),
        DELETE("x-delete-operation"),
        FETCH("x-fetch-operation"),
        PATCH("x-patch-operation");

        private final String value;
    }
    
    @Getter
    @RequiredArgsConstructor
    public enum PathType {
        LIST("list"),
        INSTANCE("instance");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum QueryParams {
        LIMIT("limit"),
        OFFSET("offset"),
        QUERY("query");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum QueryDataType {
        JAVA("String"),
        CSHARP("string");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum MustacheLocation {
        JAVA("sendgrid-java"),
        CSHARP("sendgrid-csharp"),
        GO("sendgrid-go");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BasePackage {
        JAVA("com.sendgrid.rest");

        private final String value;
    }
}
