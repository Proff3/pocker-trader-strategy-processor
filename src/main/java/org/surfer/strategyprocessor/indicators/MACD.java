package org.surfer.strategyprocessor.indicators;

import org.surfer.strategyprocessor.exceptions.WrongStrategyInitializationException;
import org.surfer.strategyprocessor.model.CandleModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MACD implements Indicator {

    private boolean isOver = false;
    private final EMA fastEMA;
    private final EMA slowEMA;
    private final EMA signalEMA;
    private BigDecimal MACD = BigDecimal.valueOf(1_000_000_000);

    /**
     * fastEMADepth = 8
     * slowEMADepth = 17
     * signalEMADepth = 9
     */
    public MACD() {
        fastEMA = new EMA(8);
        slowEMA = new EMA(17);
        signalEMA = new EMA(9);
    }

    /**
     * @param fastEMA - recommended for buy - 8 and for sale - 12
     * @param slowEMA - recommended for buy - 17 and for sale - 26
     * @param signalEMA - recommended - 9
     */
    public MACD(EMA fastEMA, EMA slowEMA, EMA signalEMA) throws WrongStrategyInitializationException {
        if (fastEMA.getIndicatorDepth() >= slowEMA.getIndicatorDepth()
                || signalEMA.getIndicatorDepth() >= slowEMA.getIndicatorDepth()) throw new WrongStrategyInitializationException();
        this.fastEMA = fastEMA;
        this.slowEMA = slowEMA;
        this.signalEMA = signalEMA;
    }

    @Override
    public void addCandle(CandleModel candleModel){
        System.out.println(signalEMA.getCurrentValue() + " signal " + MACD + " MACD");
        fastEMA.addCandle(candleModel);
        slowEMA.addCandle(candleModel);
        MACD = fastEMA.getCurrentValue().subtract(slowEMA.getCurrentValue());
        if (!fastEMA.isEnoughInformation()) return;
        CandleModel MACDCandleModel = candleModel.getCopyWithNewCloseValue(MACD);
        signalEMA.addCandle(MACDCandleModel);
    }

    @Override
    public Boolean isEnoughInformation() {
        return fastEMA.isEnoughInformation() && slowEMA.isEnoughInformation() && signalEMA.isEnoughInformation();
    }

    @Override
    public Boolean isOver() {
        return isOver;
    }

    @Override
    public int getIndicatorDepth() {
        return (slowEMA.getIndicatorDepth() + fastEMA.getIndicatorDepth()) / 2;
    }

    @Override
    public void setOver() {
        isOver = true;
    }

    public BigDecimal getSignalValue(){ return signalEMA.getCurrentValue(); }

    public BigDecimal getDifference() {
        return MACD.subtract(signalEMA.getCurrentValue()).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getDifferenceInPercents() {
        return MACD
                .subtract(signalEMA.getCurrentValue())
                .divide(MACD, 5, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100)).setScale(4, RoundingMode.HALF_UP);
    }

    public EMA getSignalEMA() {
        return signalEMA;
    }

    public BigDecimal getMACD() {
        return MACD;
    }

}
