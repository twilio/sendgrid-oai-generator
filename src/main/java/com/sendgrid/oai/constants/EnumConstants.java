package com.sendgrid.oai.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EnumConstants {

    @Getter
    @RequiredArgsConstructor
    public enum Generator {
        SENDGRID_JAVA("sendgrid-java");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        CREATE("Create"),
        READ("Read"),
        UPDATE("Update"),
        DELETE("Delete"),
        FETCH("Fetch"),
        PATCH("Patch");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PathType {
        LIST("list"),
        INSTANCE("instance");

        private final String value;
    }
}
