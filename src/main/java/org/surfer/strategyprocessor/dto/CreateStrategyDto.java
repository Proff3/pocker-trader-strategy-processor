package org.surfer.strategyprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;
import org.surfer.strategyprocessor.strategy.StrategyType;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStrategyDto {

    private String figi;
    private Map<StrategyType, List<StrategyConfig>> strategyMap;
}
