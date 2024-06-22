package org.surfer.strategyprocessor.strategy;

import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;

public interface Strategy {

    void consumeCandle(CandleModel candleModel);
    boolean isSuccess();
    StrategyType getType();
    StrategyConfig getConfig();
    void eraseSuccess();
}
