package com.github.anicmv.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description telegram 处理器
 */
public class TelegramBotHandler {
    private final CommandHandler commandHandler;
    private final MessageHandler messageHandler;
    private final TelegramBot telegramBot;

    public TelegramBotHandler(TelegramBot telegramBot, CommandHandler commandHandler, MessageHandler messageHandler) {
        this.telegramBot = telegramBot;
        this.commandHandler = commandHandler;
        this.messageHandler = messageHandler;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.message();
        if (message == null || message.text() == null) {
            return;
        }
        commandHandler.handleCommand(telegramBot, update);
        // messageHandler.handleMessage(update.getMessage());
    }
}
