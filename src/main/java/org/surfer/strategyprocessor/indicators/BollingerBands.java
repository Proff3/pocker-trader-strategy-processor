package org.surfer.strategyprocessor.indicators;

import lombok.ToString;
import org.surfer.strategyprocessor.indicators.utils.ScalableMap;
import org.surfer.strategyprocessor.model.CandleModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.function.BinaryOperator;

@ToString
public class BollingerBands implements Indicator {

    private final ScalableMap<LocalDateTime, CandleModel> candleMap;
    private final int DEPTH;
    private final BigDecimal MULTIPLIER;
    private SMA ML;
    private BigDecimal HL;
    private BigDecimal LL;
    private boolean isOver;

    public BollingerBands(int depth, int multiplier) {
        this.candleMap = new ScalableMap<>(depth);
        MULTIPLIER = new BigDecimal(multiplier);
        ML = new SMA(depth);
        DEPTH = depth;
    }

    @Override
    public void addCandle(CandleModel candleModel) {
        candleMap.addValue(candleModel.openTime(), candleModel);
        ML.addCandle(candleModel);
        calculateCurrentValues();
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() >= DEPTH && ML.isEnoughInformation();
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

    public LocalDateTime getLastZonedDateTime() {
        return ML.getCurrentValue().getKey();
    }

    public BigDecimal getMLValue() {
        return ML.getCurrentValue().getValue();
    }

    public BigDecimal getHLValue() {
        return HL;
    }

    public BigDecimal getLLValue() {
        return LL;
    }

    private void calculateCurrentValues() {
        BigDecimal productOfMultiplication = getSum()
                .divide(new BigDecimal(DEPTH), 4, RoundingMode.HALF_UP)
                .sqrt(MathContext.DECIMAL64)
                .multiply(MULTIPLIER);
        HL = ML.getCurrentValue().getValue().add(productOfMultiplication).setScale(4, RoundingMode.HALF_UP);
        LL = ML.getCurrentValue().getValue().subtract(productOfMultiplication).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal getSum() {
        return candleMap.getValues().stream()
                .map(CandleModel::closePrice)
                .reduce(BigDecimal.ZERO, squaredSumOperator)
                .setScale(4, RoundingMode.HALF_UP);
    }

    private final BinaryOperator<BigDecimal> squaredSumOperator = (acc, value) -> {
        BigDecimal squaredSum = value.subtract(ML.getCurrentValue().getValue()).setScale(4, RoundingMode.HALF_UP).pow(2);
        return acc.add(squaredSum);
    };
}
