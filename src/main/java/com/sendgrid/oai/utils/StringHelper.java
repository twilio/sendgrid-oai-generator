package com.sendgrid.oai.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class StringHelper {
    private static final String SNAKE_SEPARATOR = "_";
    private static final String SNAKE_REPLACER = "$1" + SNAKE_SEPARATOR + "$2";
    public String toSnakeCase(final String inputWord) {
        return inputWord
                .replaceAll("[^a-zA-Z\\d]+", SNAKE_SEPARATOR)
                .replaceAll("([a-z])([A-Z])", SNAKE_REPLACER)
                .replaceAll("(\\d[A-Z]*)([A-Z])", SNAKE_REPLACER)
                .replaceAll("([A-Z])([A-Z][a-z])", SNAKE_REPLACER)
                .toLowerCase();
    }
}
