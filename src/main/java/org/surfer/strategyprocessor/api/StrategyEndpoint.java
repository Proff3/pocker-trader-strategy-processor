package org.surfer.strategyprocessor.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surfer.strategyprocessor.dto.CreateStrategyDto;
import org.surfer.strategyprocessor.model.Share;
import org.surfer.strategyprocessor.service.CandleProcessor;
import org.surfer.strategyprocessor.strategy.config.StrategyConfig;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/strategy")
public class StrategyEndpoint {

    private final CandleProcessor candleProcessor;

    @PostMapping
    public ResponseEntity<String> createStrategy(@RequestParam UUID userId,
                                                 @RequestBody CreateStrategyDto body) {
        System.out.println("Create Strategy");
        candleProcessor.createStrategies(userId, Share.getByFigi(body.getFigi()), body.getStrategyMap());
        return ResponseEntity.ok("Strategy created");
    }

    @GetMapping
    public ResponseEntity<Map<Share, Map<UUID, StrategyConfig>>> getStrategies(@RequestParam UUID userId) {
        System.out.println("Get Strategies");
        return ResponseEntity.ok(candleProcessor.getStrategies(userId));
    }

    @DeleteMapping
    public ResponseEntity<String> removeStrategy(@RequestParam UUID userId,
                                                 @RequestParam String figi,
                                                 @RequestParam UUID strategyId) {
        System.out.println("Delete Strategy");
        candleProcessor.removeStrategy(userId, Share.getByFigi(figi), strategyId);
        return ResponseEntity.ok("Strategy deleted");
    }

    @GetMapping("/state")
    public ResponseEntity<Map<Share, String>> getCurrentStrategyStates(@RequestParam UUID userId) {
        System.out.println("Get Current Strategy States");
        return ResponseEntity.ok(candleProcessor.getCurrentStrategyStates(userId));
    }
}
