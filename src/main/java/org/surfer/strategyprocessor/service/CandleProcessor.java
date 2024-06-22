package org.surfer.strategyprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.model.Share;
import org.surfer.strategyprocessor.strategy.StrategyType;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleProcessor {

    private final ReentrantLock lock = new ReentrantLock();

    private final ObjectProvider<ClientsStrategyOrchestrator> orchestratorObjectProvider;
    private final Map<Share, ClientsStrategyOrchestrator> orchestrators = new ConcurrentHashMap<>();

    public void processCandle(Share share, CandleModel candleModel) {
        log.debug("process share: {}, candle: {}", share, candleModel);
        if (!orchestrators.containsKey(share)) {
            log.warn("Стратегий для {} нет", share);
        } else {
            orchestrators.get(share).consumeCandle(share, candleModel);
        }
    }

    public synchronized void createStrategies(UUID clientId, Share share, Map<StrategyType, List<StrategyConfig>> strategyMap) {
        lock.lock();
        try {
            if (!orchestrators.containsKey(share)) {
                orchestrators.put(share, orchestratorObjectProvider.getObject());
            }
            orchestrators.get(share).createStrategies(clientId, strategyMap);
        } finally {
            lock.unlock();
        }
    }

    public Map<Share, Map<UUID, StrategyConfig>> getStrategies(UUID clientId) {
        return orchestrators.entrySet()
                .stream()
                .map(entry ->  Map.entry(entry.getKey(), entry.getValue().getStrategies(clientId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Share, String> getCurrentStrategyStates(UUID clientId) {
        return orchestrators.entrySet()
                .stream()
                .map(entry ->  Map.entry(entry.getKey(), entry.getValue().getCurrentStrategyStates(clientId).toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public synchronized void removeStrategy(UUID clientId, Share share, UUID strategyId) {
        lock.lock();
        try {
            orchestrators.get(share).removeStrategy(clientId, strategyId);
        } finally {
            lock.unlock();
        }
    }
}
