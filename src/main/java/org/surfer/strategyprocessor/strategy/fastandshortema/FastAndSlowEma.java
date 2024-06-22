package org.surfer.strategyprocessor.strategy.fastandshortema;

import org.surfer.strategyprocessor.indicators.EMA;
import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.strategy.Strategy;
import org.surfer.strategyprocessor.strategy.config.FastAndSlowEmaStrategyConfig;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;

import java.math.BigDecimal;

abstract class FastAndSlowEma implements Strategy {

    protected final FastAndSlowEmaStrategyConfig cfg;
    protected final EMA slow;
    protected final EMA fast;
    protected BigDecimal previousFastValue;
    protected BigDecimal previousSlowValue;
    protected CandleModel successCandleModel;

    public FastAndSlowEma(FastAndSlowEmaStrategyConfig cfg) {
        this.cfg = cfg;
        slow = new EMA(cfg.slow());
        fast = new EMA(cfg.fast());
    }

    @Override
    public void consumeCandle(CandleModel candleModel) {
        slow.addCandle(candleModel);
        fast.addCandle(candleModel);
    }

    @Override
    public StrategyConfig getConfig() {
        return cfg;
    }

    @Override
    public String toString() {
        return "FastAndSlowEma{" +
                "cfg=" + cfg +
                ", slow=" + slow +
                ", fast=" + fast +
                ", previousFastValue=" + previousFastValue +
                ", previousSlowValue=" + previousSlowValue +
                ", successCandle=" + successCandleModel +
                '}';
    }

    @Override
    public void eraseSuccess() {
        successCandleModel = null;
    }
}
