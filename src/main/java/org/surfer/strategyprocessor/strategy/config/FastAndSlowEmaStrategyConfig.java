package org.surfer.strategyprocessor.strategy.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.surfer.strategyprocessor.model.Share;

public record FastAndSlowEmaStrategyConfig (
        @JsonProperty("slow") int slow,
        @JsonProperty("fast") int fast,
        @JsonProperty("differenceCoefficient") double differenceCoefficient,
        @JsonProperty("minutesToLive") int minutesToLive,
        @JsonProperty("border") double border)
        implements StrategyConfig {
}