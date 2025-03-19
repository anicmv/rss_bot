package com.github.anicmv.bot.command;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.anicmv.entity.RssSource;
import com.github.anicmv.entity.User;
import com.github.anicmv.entity.UserSubscription;
import com.github.anicmv.mapper.RssSourceMapper;
import com.github.anicmv.mapper.UserMapper;
import com.github.anicmv.mapper.UserSubscriptionMapper;
import com.github.anicmv.utils.RssUtil;
import com.pengrad.telegrambot.model.Update;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anicmv
 * @date 2024/9/18 16:09
 * @description 接受到telegram bot发送的 /sub 指令
 */
public class SubscribeCommand implements Command {

    private final UserMapper userMapper;

    private final RssSourceMapper rssSourceMapper;

    private final UserSubscriptionMapper userSubscriptionMapper;

    public SubscribeCommand(UserMapper userMapper, RssSourceMapper rssSourceMapper, UserSubscriptionMapper userSubscriptionMapper) {
        this.userMapper = userMapper;
        this.rssSourceMapper = rssSourceMapper;
        this.userSubscriptionMapper = userSubscriptionMapper;
    }

    @Override
    public String execute(Update update) {
        long chatId = update.message().chat().id();
        User user = fetchUser(chatId);

        if (user == null) {
            return "You haven't subscribed to anything yet.";
        }

        String commandText = update.message().text();

        String regex = "/(\\S+)\\s(https://\\S+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(commandText);

        if (!matcher.find()) {
            return "There's something wrong with the path you entered.";
        }

        String sourceUrl = matcher.group(2);
        RssSource source = fetchRssSource(sourceUrl);

        if (source != null) {
            processUserSubscription(user, source);
            return "This site is subscribed!";
        }

        return saveFeedInfo(sourceUrl, chatId);
    }

    private User fetchUser(long chatId) {
        // 查询用户表是否注册
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("telegram_id", chatId);

        return userMapper.selectOne(userQueryWrapper);
    }

    private RssSource fetchRssSource(String sourceUrl) {
        QueryWrapper<RssSource> rssSourceQueryWrapper = new QueryWrapper<>();
        rssSourceQueryWrapper.eq("source_url", sourceUrl);
        return rssSourceMapper.selectOne(rssSourceQueryWrapper);
    }

    private void processUserSubscription(User user, RssSource source) {
        UserSubscription subscription = fetchUserSubscription(user.getTelegramId(), source.getId());

        if (subscription != null) {
            return;
        }

        UserSubscription userSubscription = UserSubscription.builder()
                .telegramId(user.getTelegramId())
                .sourceId(source.getId())
                .subscribedAt(new Date())
                .notifiedAt(RssUtil.getDefaultDate())
                .build();

        userSubscriptionMapper.insert(userSubscription);
    }

    private String saveFeedInfo(String url, long chatId) {
        SyndFeed feed = RssUtil.buildSyndFeed(url);
        if (feed == null) {
            return "This link is not supported.";
        }
        RssSource rssSource = RssSource.builder()
                .sourceUrl(url)
                .sourceName(feed.getTitle())
                .description(feed.getDescription())
                .lastChecked(new Date())
                .build();
        rssSourceMapper.insert(rssSource);

        UserSubscription userSubscription = UserSubscription.builder()
                .telegramId(chatId)
                .sourceId(rssSource.getId())
                .subscribedAt(new Date())
                .notifiedAt(RssUtil.getDefaultDate())
                .build();
        userSubscriptionMapper.insert(userSubscription);
        return "Subscription added successfully!";
    }

    private UserSubscription fetchUserSubscription(long chatId, Integer sourceId) {
        QueryWrapper<UserSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telegram_id", chatId);
        queryWrapper.eq("source_id", sourceId);
        return userSubscriptionMapper.selectOne(queryWrapper);
    }
}