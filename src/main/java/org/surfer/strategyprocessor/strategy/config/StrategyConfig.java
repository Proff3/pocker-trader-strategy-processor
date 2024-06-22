package org.surfer.strategyprocessor.strategy.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FastAndSlowEmaStrategyConfig.class, name = "FastAndSlowEmaStrategyConfig")
})
public interface StrategyConfig {
}
