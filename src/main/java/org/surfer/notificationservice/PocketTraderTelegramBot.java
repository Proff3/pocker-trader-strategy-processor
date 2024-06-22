package org.surfer.notificationservice;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashSet;
import java.util.Set;

@Service
public class PocketTraderTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final Set<Long> usersToNotify = new HashSet<>();

    public PocketTraderTelegramBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if ("/add_to_notification".equals(message_text)) {
                usersToNotify.add(chat_id);
                sendMessage(chat_id, "You are now added to the notification queue.");
            }
        }
    }

    @Override
    public String getBotToken() {
        return "6867278121:AAFZoCRBHBsrlqt7sOryuutEyt1dkCEKcG0";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void notify(String msg) {
        usersToNotify.forEach(user -> sendMessage(user, msg));
    }
}
