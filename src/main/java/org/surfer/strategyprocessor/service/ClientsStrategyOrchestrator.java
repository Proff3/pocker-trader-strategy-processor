package org.surfer.strategyprocessor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.model.Share;
import org.surfer.strategyprocessor.service.strategy.StrategyFactoryService;
import org.surfer.strategyprocessor.strategy.Strategy;
import org.surfer.strategyprocessor.strategy.StrategyType;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ClientsStrategyOrchestrator {

    private final StrategyFactoryService strategyFactoryService;
    private final SuccessStrategyStateHandler successStrategyStateHandler;
    private final Map<UUID, Map<UUID, Strategy>> clientsStrategies = new ConcurrentHashMap<>();

    public void createStrategies(UUID id, Map<StrategyType, List<StrategyConfig>> strategyMap) {
        System.out.println("Creating strategies for client " + id);
        clientsStrategies.putIfAbsent(id, new ConcurrentHashMap<>());

        Set<StrategyConfig> existsStrategies = clientsStrategies.get(id)
                .values()
                .stream()
                .map(Strategy::getConfig)
                .collect(Collectors.toSet());

        strategyMap.forEach((type, configs) -> configs
                .stream()
                .filter(cfg -> !existsStrategies.contains(cfg))
                .map(cfg -> strategyFactoryService.create(type, cfg))
                .forEach(strategy -> clientsStrategies.get(id).put(UUID.randomUUID(), strategy)));
    }

    public void consumeCandle(Share share, CandleModel candleModel) {
        clientsStrategies
                .values()
                .forEach(entry -> entry
                        .values()
                        .forEach(strategy -> {
                            strategy.consumeCandle(candleModel);
                            if (strategy.isSuccess()) {
                                successStrategyStateHandler.handle(share, strategy, candleModel);
                                strategy.eraseSuccess();
                            }
                        }));
    }

    public Map<UUID, StrategyConfig> getStrategies(UUID clientId) {
        return clientsStrategies.get(clientId)
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getConfig()));
    }

    public List<String> getCurrentStrategyStates(UUID clientId) {
        return clientsStrategies
                .get(clientId)
                .values()
                .stream()
                .map(Object::toString)
                .toList();
    }

    public void removeStrategy(UUID clientId, UUID strategyId) {
        Map<UUID, Strategy> allClientStrategies = clientsStrategies.get(clientId);
        if (allClientStrategies.size() == 1) {
            clientsStrategies.remove(clientId);
        } else {
            allClientStrategies.remove(strategyId);
        }
    }
}
