package com.github.anicmv.bot.command;

import com.github.anicmv.entity.User;
import com.github.anicmv.mapper.UserMapper;
import com.pengrad.telegrambot.model.Update;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 接受到telegram bot发送的 /start 指令
 */
public class StartCommand implements Command {
    private final UserMapper userMapper;

    public StartCommand(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String execute(Update update) {

        long chatId = update.message().chat().id();

        String firstName = update.message().from().firstName();
        String lastName = update.message().from().lastName();
        String username = (firstName == null ? "" : firstName) + (lastName == null ? "" : lastName);

        User user = User.builder()
                .telegramId(chatId)
                .username(username)
                .build();

        userMapper.insertOrUpdate(user);
        return "You're already registered.";
    }
}