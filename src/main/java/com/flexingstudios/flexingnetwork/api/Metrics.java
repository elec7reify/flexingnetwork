package com.flexingstudios.flexingnetwork.api;

public interface Metrics {
    default void add(String key) {
        add(key, 1);
    }

    void add(String value, int val);
}
