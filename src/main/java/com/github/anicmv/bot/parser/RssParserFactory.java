package com.github.anicmv.bot.parser;

/**
 * @author anicmv
 * @date 2024/7/23 20:50
 * @description rss格式化工厂
 */
public class RssParserFactory {
    public static RssParser getParser(String sourceUrl) {
        if (sourceUrl.contains("www.summer-plus.net")) {
            return new SouthPlusRssParser();
        } else if (sourceUrl.contains("www.nodeseek.com")) {
            return new NodeSeekRssParser();
        }else if (sourceUrl.contains("www.52pojie.cn")) {
            return new PoJie52RssParser();
        }else {
            return new DefaultRssParser();
        }
    }
}
