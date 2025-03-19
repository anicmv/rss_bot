package com.github.anicmv.bot.command;

import com.pengrad.telegrambot.model.Update;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 指令接口
 */
public interface Command {
    String execute(Update update);
}
