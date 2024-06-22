package org.surfer.strategyprocessor.indicators.utils;

public class ParameterPair<K, V> {
    private final K key;
    private final V value;

    public ParameterPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
