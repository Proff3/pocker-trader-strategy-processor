package org.surfer.strategyprocessor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CandleModel(LocalDateTime openTime, BigDecimal openPrice, BigDecimal highPrice, BigDecimal lowPrice,
                          BigDecimal closePrice, BigDecimal volume, LocalDateTime closeTime, LocalDateTime currentTime) {
    public CandleModel getCopyWithNewCloseValue(BigDecimal closePrice) {
        return new CandleModel(this.openTime, this.openPrice, this.highPrice, this.lowPrice, closePrice, this.volume, this.closeTime, this.currentTime);
    }
}
