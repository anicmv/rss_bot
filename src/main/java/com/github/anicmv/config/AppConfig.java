package com.github.anicmv.config;

import com.github.anicmv.bot.CommandHandler;
import com.github.anicmv.bot.MessageHandler;
import com.github.anicmv.bot.TelegramBotHandler;
import com.github.anicmv.mapper.RssSourceMapper;
import com.github.anicmv.mapper.UserMapper;
import com.github.anicmv.mapper.UserSubscriptionMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description 配置类
 */
@Slf4j
@Getter
@Setter
@Configuration
public class AppConfig {

    @Bean
    public CommandHandler commandHandler(UserMapper userMapper, RssSourceMapper rssSourceMapper, UserSubscriptionMapper userSubscriptionMapper) {
        return new CommandHandler(userMapper, rssSourceMapper, userSubscriptionMapper);
    }

    @Bean
    public MessageHandler messageHandler() {
        return new MessageHandler();
    }

    @Bean
    public TelegramBot telegramBot(CommandHandler commandHandler, MessageHandler messageHandler, GlobalConfig config) {
        TelegramBot telegramBot = new TelegramBot(config.getToken());
        telegramBot.setUpdatesListener(updates -> handleUpdates(telegramBot, updates, commandHandler, messageHandler), this::handleError);
        return telegramBot;
    }

    private int handleUpdates(TelegramBot telegramBot, List<Update> updates, CommandHandler commandHandler, MessageHandler messageHandler) {
        TelegramBotHandler botHandler = new TelegramBotHandler(telegramBot, commandHandler, messageHandler);
        updates.forEach(botHandler::onUpdateReceived);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleError(TelegramException e) {
        if (e.response() != null) {
            log.error("Error code: {}, Description: {}", e.response().errorCode(), e.response().description());
        } else {
            log.error(e.getMessage());
        }
    }
}
