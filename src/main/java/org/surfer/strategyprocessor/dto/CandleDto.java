package org.surfer.strategyprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.model.Share;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CandleDto {

    private String figi;
    private Candle candle;

    public CandleModel toModel() {
        return new CandleModel(
                candle.getOpenTime(),
                candle.getOpenPrice(),
                candle.getHighPrice(),
                candle.getLowPrice(),
                candle.getClosePrice(),
                candle.getVolume(),
                candle.getCloseTime(),
                candle.getCurrentTime());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Candle {
        private LocalDateTime openTime;
        private BigDecimal openPrice;
        private BigDecimal highPrice;
        private BigDecimal lowPrice;
        private BigDecimal closePrice;
        private BigDecimal volume;
        private LocalDateTime closeTime;
        private LocalDateTime currentTime;
    }

    public Share getShare() {
        return Share.getByFigi(figi);
    }
}


