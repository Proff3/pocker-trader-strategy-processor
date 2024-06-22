package org.surfer.strategyprocessor.indicators;

import lombok.ToString;
import org.surfer.strategyprocessor.model.CandleModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class EMA implements Indicator {

    private final List<CandleModel> listOfHistoricCandleModels = new ArrayList<>();
    private final int timePeriod;
    private final int depth;
    private final BigDecimal alpha;

    private BigDecimal currentValue = BigDecimal.ZERO.setScale(5,RoundingMode.HALF_UP);
    private BigDecimal previousValue = BigDecimal.ZERO.setScale(5,RoundingMode.HALF_UP);
    private boolean isOver;

    public EMA(int timePeriod) {
        this.timePeriod = timePeriod;
        alpha = BigDecimal.valueOf(2.0 / (timePeriod + 1));
        depth = timePeriod * 5;
    }

    @Override
    public void addCandle(CandleModel candleModel){
        if (listOfHistoricCandleModels.isEmpty()) {
            currentValue = candleModel.closePrice();
            listOfHistoricCandleModels.add(candleModel);
            return;
        }
        CandleModel lastCandleModel = listOfHistoricCandleModels.get(listOfHistoricCandleModels.size() - 1);
        if (lastCandleModel.openTime().equals(candleModel.openTime())) {
            listOfHistoricCandleModels.remove(lastCandleModel);
            currentValue = previousValue;
        } else {
            previousValue = currentValue;
        }
        listOfHistoricCandleModels.add(candleModel);
        if(timePeriod >= listOfHistoricCandleModels.size()){
            Double currentValueInDouble = listOfHistoricCandleModels
                    .stream()
                    .map(c -> c.closePrice().doubleValue())
                    .collect(Collectors.averagingDouble(currentCandleDouble -> currentCandleDouble));
            currentValue = BigDecimal.valueOf(currentValueInDouble);
            previousValue = currentValue;
        } else {
            BigDecimal alphaValue = candleModel.closePrice().multiply(alpha).setScale(5,RoundingMode.HALF_UP);
            BigDecimal candleValue = currentValue.multiply(BigDecimal.ONE.subtract(alpha)).setScale(5,RoundingMode.HALF_UP);
            currentValue = alphaValue.add(candleValue);
        }
        if (listOfHistoricCandleModels.size() >= depth) listOfHistoricCandleModels.remove(0);
    }

    @Override
    public Boolean isEnoughInformation() {
        return listOfHistoricCandleModels.size() > timePeriod;
    }

    @Override
    public Boolean isOver() {
        return isOver;
    }

    @Override
    public int getIndicatorDepth() {
        return depth;
    }

    @Override
    public void setOver() {
        isOver = true;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }
}
