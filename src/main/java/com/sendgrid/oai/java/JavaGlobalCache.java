package com.sendgrid.oai.java;

import java.util.HashMap;

// Use to for accessing common property
public class JavaGlobalCache<K, V> extends HashMap<K, V> {

    private static volatile JavaGlobalCache instance;

    private JavaGlobalCache() {}

    public static <K, V> JavaGlobalCache<K, V> getInstance() {
        if (instance == null) {
            synchronized (JavaGlobalCache.class) {
                if (instance == null) {
                    instance = new JavaGlobalCache<>();
                }
            }
        }
        return instance;
    }

    public void putValue(K key, V value) {
        this.put(key, value);
    }

    public V getValue(K key) {
        return this.get(key);
    }
}