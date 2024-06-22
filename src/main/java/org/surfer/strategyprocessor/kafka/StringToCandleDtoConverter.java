package org.surfer.strategyprocessor.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.surfer.strategyprocessor.dto.CandleDto;

@Component
public class StringToCandleDtoConverter implements Converter<String, CandleDto> {

    private final ObjectMapper mapepr = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public CandleDto convert(String source) {
        try {
            return mapepr.readValue(source, CandleDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
