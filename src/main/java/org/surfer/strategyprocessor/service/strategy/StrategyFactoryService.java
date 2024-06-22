package org.surfer.strategyprocessor.service.strategy;

import org.springframework.stereotype.Service;
import org.surfer.strategyprocessor.strategy.Strategy;
import org.surfer.strategyprocessor.strategy.StrategyType;
import org.surfer.strategyprocessor.strategy.config.FastAndSlowEmaStrategyConfig;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;
import org.surfer.strategyprocessor.strategy.fastandshortema.FastAndSlowEmaLong;
import org.surfer.strategyprocessor.strategy.fastandshortema.FastAndSlowEmaShort;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Service
public class StrategyFactoryService {

    private static final Map<StrategyType, Class<? extends Strategy>> STRATEGIES = new HashMap<>();
    private static final Map<StrategyType, Class<? extends StrategyConfig>> STRATEGIES_CONFIGS = new HashMap<>();

    public StrategyFactoryService() {

        STRATEGIES.put(StrategyType.FAST_AND_SLOW_EMA_LONG, FastAndSlowEmaLong.class);
        STRATEGIES_CONFIGS.put(StrategyType.FAST_AND_SLOW_EMA_LONG, FastAndSlowEmaStrategyConfig.class);

        STRATEGIES.put(StrategyType.FAST_AND_SLOW_EMA_SHORT, FastAndSlowEmaShort.class);
        STRATEGIES_CONFIGS.put(StrategyType.FAST_AND_SLOW_EMA_SHORT, FastAndSlowEmaStrategyConfig.class);
    }

    public <SC extends StrategyConfig> Strategy create(StrategyType strategyType, SC config) {
        try {
            Object initParam = STRATEGIES_CONFIGS.get(strategyType).cast(config);
            Class<? extends Strategy> strategy = STRATEGIES.get(strategyType);
            return strategy.getDeclaredConstructor(initParam.getClass()).newInstance(initParam);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
