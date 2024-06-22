package org.surfer.strategyprocessor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.surfer.notificationservice.PocketTraderTelegramBot;
import org.surfer.strategyprocessor.model.CandleModel;
import org.surfer.strategyprocessor.model.Share;
import org.surfer.strategyprocessor.strategy.Strategy;

@Service
@RequiredArgsConstructor
public class SuccessStrategyStateHandler {

    private final PocketTraderTelegramBot tgBot;

    public void handle(Share share, Strategy strategy, CandleModel candleModel) {
        tgBot.notify(
                ("Акция %s\nСтратегия успешно отработала %s\nСо свечой %s").formatted(share, strategy.toString(), candleModel.toString())
        );
    }
}
