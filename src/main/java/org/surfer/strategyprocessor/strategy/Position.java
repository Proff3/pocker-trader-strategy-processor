package org.surfer.strategyprocessor.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Position {
    @JsonProperty("short")
    SHORT,
    @JsonProperty("long")
    LONG
}
