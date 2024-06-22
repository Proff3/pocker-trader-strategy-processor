package org.surfer.strategyprocessor.indicators;

import lombok.ToString;
import org.surfer.strategyprocessor.indicators.utils.Pair;
import org.surfer.strategyprocessor.indicators.utils.ScalableMap;
import org.surfer.strategyprocessor.model.CandleModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@ToString
public class StochasticOscillator implements Indicator {

    private final int DEPTH;
    private final int SMOOTH;
    private final SMA SMA;
    private final BigDecimal MULTIPLIER = new BigDecimal(100);

    private BigDecimal max;
    private BigDecimal min;
    private Pair<LocalDateTime, BigDecimal> value;
    private Pair<LocalDateTime, BigDecimal> previousValue;
    private final ScalableMap<LocalDateTime, CandleModel> candleMap;
    private boolean isOver = false;
    private final ScalableMap<LocalDateTime, BigDecimal> values;

    public StochasticOscillator(int depth, int emaDepth, int smooth) {
        DEPTH = depth;
        SMOOTH = smooth;
        SMA = new SMA(emaDepth);
        candleMap = new ScalableMap<>(depth);
        values = new ScalableMap<>(smooth);
    }

    public StochasticOscillator(int depth, int emaDepth) {
        DEPTH = depth;
        SMOOTH = 1;
        SMA = new SMA(emaDepth);
        candleMap = new ScalableMap<>(depth);
        values = new ScalableMap<>(1);
    }

    @Override
    public void addCandle(CandleModel candleModel) {
        if(max == null || min == null) {
            updateLimits(candleModel);
            calculate(candleModel);
        } else {
            calculate(candleModel);
        }
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() >= DEPTH && SMA.isEnoughInformation();
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

    public boolean isValueLowerEma() {
        return getEmaValue().getValue().compareTo(value.getValue()) > 0;
    }

    public boolean isEmaLowerValue() {
        return value.getValue().compareTo(getEmaValue().getValue()) > 0;
    }

    private void calculate(CandleModel candleModel) {
        candleMap.addValue(candleModel.openTime(), candleModel);
        updateLimits(candleModel);
        updatePreviousValue(candleModel);
        value = new Pair<>(candleModel.openTime(), calculateCurrentValue(candleModel));
        CandleModel candleModelWithCalculatedValue = candleModel.getCopyWithNewCloseValue(value.getValue());
        SMA.addCandle(candleModelWithCalculatedValue);
    }

    private void updateLimits(CandleModel candleModel) {
        BigDecimal currentMax = candleMap.getValues().stream().map(CandleModel::highPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal currentMin = candleMap.getValues().stream().map(CandleModel::highPrice).min(BigDecimal::compareTo).orElse(new BigDecimal(1_000_000));
        max = candleModel.highPrice().compareTo(currentMax) > 0 ? candleModel.highPrice() : currentMax;
        min = candleModel.lowPrice().compareTo(currentMin) < 0 ? candleModel.lowPrice() : currentMin;
    }

    private BigDecimal calculateCurrentValue(CandleModel candleModel) {
        BigDecimal numerator = candleModel.closePrice().subtract(min);
        BigDecimal denominator = max.compareTo(min) == 0 ? BigDecimal.ONE : max.subtract(min);
        BigDecimal calculatedValue = MULTIPLIER.multiply(numerator.divide(denominator, 4, RoundingMode.HALF_UP));
        values.addValue(candleModel.openTime(), calculatedValue);
        return values.getValues().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(SMOOTH), 4, RoundingMode.HALF_UP);
    }

    private void updatePreviousValue(CandleModel candleModel) {
        if (previousValue != null && candleModel.openTime().isAfter(value.getKey())) {
            previousValue = value;
        }
        if (previousValue == null) {
            previousValue = value;
        }
    }

    public Pair<LocalDateTime, BigDecimal> getValue() {
        return value;
    }

    public Pair<LocalDateTime, BigDecimal> getEmaValue() {
        return SMA.getCurrentValue();
    }

    public BigDecimal getDiffBetweenCurrentAndPreviousInPercent() {
        if (previousValue != null) {
            BigDecimal diff = previousValue.getValue().subtract(value.getValue()).setScale(4, RoundingMode.HALF_UP).abs();
            return diff.divide(previousValue.getValue(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}
