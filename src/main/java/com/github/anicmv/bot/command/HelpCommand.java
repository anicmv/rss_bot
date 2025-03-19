package com.github.anicmv.bot.command;

import com.pengrad.telegrambot.model.Update;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 接受到telegram bot发送的 /help 指令
 */
public class HelpCommand implements Command {

    @Override
    public String execute(Update update) {
        return """
                /sub - add subscription -> [/sub https://example.com]
                /list - get a list of my subscriptions
                /unsub - unsubscribe -> [/unsub 1]
                /help - get this help""";
    }
}