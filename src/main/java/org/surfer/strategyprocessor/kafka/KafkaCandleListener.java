package org.surfer.strategyprocessor.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.surfer.strategyprocessor.dto.CandleDto;
import org.surfer.strategyprocessor.service.CandleProcessor;

@Service
@RequiredArgsConstructor
public class KafkaCandleListener {

    private final CandleProcessor processor;

    @KafkaListener(id = "${group-id}", topics = "${topic}", containerFactory = "candleListenerContainer")
    public void listen(CandleDto candleDto) {
        processor.processCandle(candleDto.getShare(), candleDto.toModel());
    }
}
