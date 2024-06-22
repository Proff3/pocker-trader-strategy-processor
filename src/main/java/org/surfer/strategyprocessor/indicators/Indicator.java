package org.surfer.strategyprocessor.indicators;

import org.surfer.strategyprocessor.model.CandleModel;

public interface Indicator {
    void addCandle(CandleModel candleModel);
    Boolean isEnoughInformation();
    Boolean isOver();
    int getIndicatorDepth();
    void setOver();
}
