package com.github.anicmv.bot;

import com.github.anicmv.bot.command.*;
import com.github.anicmv.mapper.RssSourceMapper;
import com.github.anicmv.mapper.UserMapper;
import com.github.anicmv.mapper.UserSubscriptionMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description 指令处理器
 */
public class CommandHandler {
    private final Map<String, Command> commandMap;

    public CommandHandler(UserMapper userMapper, RssSourceMapper rssSourceMapper, UserSubscriptionMapper userSubscriptionMapper) {
        commandMap = new HashMap<>();
        commandMap.put("/start", new StartCommand(userMapper));
        commandMap.put("/list", new ListCommand(userMapper, userSubscriptionMapper, rssSourceMapper));
        commandMap.put("/sub", new SubscribeCommand(userMapper, rssSourceMapper, userSubscriptionMapper));
        commandMap.put("/unsub", new UnsubscribeCommand(userSubscriptionMapper, rssSourceMapper));
        commandMap.put("/help", new HelpCommand());
    }

    public void handleCommand(TelegramBot telegramBot, Update update) {
        String commandText = update.message().text().trim();
        Long chatId = update.message().chat().id();
        Command command = commandMap.get(commandText.split(" ")[0]);
        if (command != null) {
            String result = command.execute(update);

            SendMessage sendMessage = new SendMessage(chatId, result)
                    .parseMode(ParseMode.Markdown)
                    .replyToMessageId(update.message().messageId());

            telegramBot.execute(sendMessage);
        }
    }


}
