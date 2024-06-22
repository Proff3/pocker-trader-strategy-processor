package org.surfer.strategyprocessor.indicators;

import org.surfer.strategyprocessor.indicators.utils.Pair;
import org.surfer.strategyprocessor.indicators.utils.ScalableMap;
import org.surfer.strategyprocessor.model.CandleModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class SMA implements Indicator {

    private final int DEPTH;
    private final ScalableMap<LocalDateTime, CandleModel> candleMap;

    private Pair<LocalDateTime, BigDecimal> currentValue;
    private Pair<LocalDateTime, BigDecimal> previousValue;
    private boolean isOver;

    public SMA(int depth) {
        DEPTH = depth;
        candleMap = new ScalableMap<>(depth);
    }

    @Override
    public void addCandle(CandleModel candleModel) {
        candleMap.addValue(candleModel.openTime(), candleModel);
        double currentValueInDouble = candleMap.getValues()
                .stream()
                .collect(Collectors.averagingDouble(c -> c.closePrice().doubleValue()));
        BigDecimal value = new BigDecimal(currentValueInDouble).setScale(4, RoundingMode.HALF_UP);
        updatePrevious(candleModel);
        currentValue = new Pair<>(candleModel.openTime(), value);
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() > DEPTH;
    }

    @Override
    public Boolean isOver() {
        return isOver;
    }

    @Override
    public int getIndicatorDepth() {
        return DEPTH;
    }

    @Override
    public void setOver() {
        isOver = true;
    }

    public Pair<LocalDateTime, BigDecimal> getCurrentValue() {
        return currentValue;
    }

    public Pair<LocalDateTime, BigDecimal> getPreviousValue() {
        return previousValue;
    }

    private void updatePrevious(CandleModel candleModel) {
        if (previousValue != null && candleModel.openTime().isAfter(currentValue.getKey())) {
            previousValue = currentValue;
        }
        if (previousValue == null) {
            previousValue = currentValue;
        }
    }
}
