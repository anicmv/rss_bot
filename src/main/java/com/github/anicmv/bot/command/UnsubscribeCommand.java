package com.github.anicmv.bot.command;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.anicmv.entity.UserSubscription;
import com.github.anicmv.mapper.RssSourceMapper;
import com.github.anicmv.mapper.UserSubscriptionMapper;
import com.pengrad.telegrambot.model.Update;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 接受到telegram bot发送的 /unsub 指令
 */
public class UnsubscribeCommand implements Command {

    private final UserSubscriptionMapper userSubscriptionMapper;
    private final RssSourceMapper rssSourceMapper;

    public UnsubscribeCommand(UserSubscriptionMapper userSubscriptionMapper, RssSourceMapper rssSourceMapper) {
        this.userSubscriptionMapper = userSubscriptionMapper;
        this.rssSourceMapper = rssSourceMapper;
    }


    @Override
    public String execute(Update update) {
        long chatId = update.message().chat().id();
        // 查询该用户的订阅
        String commandText = update.message().text();

        String regex = "/unsub\\s+(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(commandText);

        if (matcher.matches()) {
            String sourceId = matcher.group(1);

            // 删除用户订阅
            String result = deleteUserSubscription(sourceId, chatId);
            // 删除源
            deleteRssSource(sourceId);

            return result;
        } else {
            return "This command is wrong.";
        }
    }

    private String deleteUserSubscription(String sourceId, long chatId) {
        // 删除用户订阅
        QueryWrapper<UserSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_id", sourceId);
        queryWrapper.eq("telegram_id", chatId);
        UserSubscription userSubscription = userSubscriptionMapper.selectOne(queryWrapper);

        if (userSubscription == null) {
            return "You didn't subscribe to this.";
        }
        userSubscriptionMapper.delete(queryWrapper);
        return "Subscription deleted.";
    }

    private void deleteRssSource(String sourceId) {
        QueryWrapper<UserSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_id", sourceId);
        List<UserSubscription> userSubscriptions = userSubscriptionMapper.selectList(queryWrapper);
        if (userSubscriptions.isEmpty()) {
            // 删除源
            try {
                rssSourceMapper.deleteById(sourceId);
            } catch (Exception ignored) {
            }
        }
    }
}