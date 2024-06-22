package org.surfer.strategyprocessor.model;

import org.surfer.strategyprocessor.strategy.config.StrategyConfig;
import org.surfer.strategyprocessor.strategy.StrategyType;

import java.util.Map;

public record CreateStrategyModel(Map<StrategyType, StrategyConfig> strategyMap) {
}
