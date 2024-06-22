package org.surfer.strategyprocessor.strategy.fastandshortema;

import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.strategy.StrategyType;
import org.surfer.strategyprocessor.strategy.config.FastAndSlowEmaStrategyConfig;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.surfer.strategyprocessor.utils.StrategyUtils.isFirstBigger;
import static org.surfer.strategyprocessor.utils.StrategyUtils.isFirstBiggerWithCoefficient;

public class FastAndSlowEmaLong extends FastAndSlowEma {

    public FastAndSlowEmaLong(FastAndSlowEmaStrategyConfig cfg) {
        super(cfg);
    }

    @Override
    public void consumeCandle(CandleModel candleModel) {

        previousFastValue = fast.getCurrentValue();
        previousSlowValue = slow.getCurrentValue();

        super.consumeCandle(candleModel);

        if (isStrategySuccess()) {
            successCandleModel = candleModel;
        }

        if (successCandleModel != null && LocalDateTime.now(UTC).isAfter(successCandleModel.closeTime().plusMinutes(cfg.minutesToLive()))) {
            successCandleModel = null;
        }
    }

    @Override
    public boolean isSuccess() {
        if (previousFastValue == null || previousSlowValue == null) return false;
        boolean isSuccessAlive = successCandleModel != null && (LocalDateTime.now(UTC).isBefore(successCandleModel.closeTime().plusMinutes(cfg.minutesToLive()))
                || LocalDateTime.now(UTC).isEqual(successCandleModel.closeTime().plusMinutes(cfg.minutesToLive())));
        return isStrategySuccess() || isSuccessAlive;
    }

    private boolean isStrategySuccess() {
        return isFirstBigger(previousSlowValue, previousFastValue) // previous value comparison
                && isFirstBiggerWithCoefficient(fast.getCurrentValue(), slow.getCurrentValue(), cfg.differenceCoefficient()) // current value comparison
                && cfg.border() >= previousFastValue.doubleValue();
    }

    @Override
    public StrategyType getType() {
        return StrategyType.FAST_AND_SLOW_EMA_LONG;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
