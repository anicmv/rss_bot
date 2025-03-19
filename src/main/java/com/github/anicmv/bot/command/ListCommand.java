package com.github.anicmv.bot.command;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.anicmv.entity.RssSource;
import com.github.anicmv.entity.User;
import com.github.anicmv.entity.UserSubscription;
import com.github.anicmv.mapper.RssSourceMapper;
import com.github.anicmv.mapper.UserMapper;
import com.github.anicmv.mapper.UserSubscriptionMapper;
import com.pengrad.telegrambot.model.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 接受到telegram bot发送的 /list 指令
 */
public class ListCommand implements Command {

    private final UserMapper userMapper;
    private final UserSubscriptionMapper userSubscriptionMapper;

    private final RssSourceMapper rssSourceMapper;

    public ListCommand(UserMapper userMapper, UserSubscriptionMapper userSubscriptionMapper, RssSourceMapper rssSourceMapper) {
        this.userMapper = userMapper;
        this.userSubscriptionMapper = userSubscriptionMapper;
        this.rssSourceMapper = rssSourceMapper;
    }

    @Override
    public String execute(Update update) {
        long chatId = update.message().chat().id();
        User user = fetchUser(chatId);
        if (user == null) {
            return "You're not registered.";
        }

        List<UserSubscription> userSubscriptions = fetchUserSubscriptions(chatId);

        // 获取所有的 sourceId
        List<Integer> sourceIds = userSubscriptions.stream()
                .map(UserSubscription::getSourceId)
                .collect(Collectors.toList());
        if (sourceIds.isEmpty()) {
            return "You haven't subscribed to anything yet.";
        }
        // 查询所有的订阅源信息
        List<RssSource> rssSourceList = fetchRssSources(sourceIds);

        return rssSourceList.stream()
                .map(rssSource -> "\\[`" + rssSource.getId() + "`] " + "[" + rssSource.getSourceName() + "]" + "(" + rssSource.getSourceUrl() + ")")
                .collect(Collectors.joining("\n"));
    }

    private User fetchUser(long chatId) {
        // 查询用户表是否注册
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("telegram_id", chatId);

        return userMapper.selectOne(userQueryWrapper);
    }

    private List<RssSource> fetchRssSources(List<Integer> sourceIds) {
        QueryWrapper<RssSource> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", sourceIds);
        return rssSourceMapper.selectList(queryWrapper);
    }

    private List<UserSubscription> fetchUserSubscriptions(long chatId) {
        QueryWrapper<UserSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telegram_id", chatId);
        return userSubscriptionMapper.selectList(queryWrapper);
    }
}