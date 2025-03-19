package com.github.anicmv.bot.parser;

import com.github.anicmv.entity.RssItem;
import com.github.anicmv.utils.HashUtil;
import com.github.anicmv.utils.RssUtil;
import com.rometools.rome.feed.synd.SyndFeed;

import java.io.InputStream;
import java.util.List;

/**
 * @author anicmv
 * @date 2024/7/23 20:50
 * @description NodeSeek rss
 */
public class NodeSeekRssParser implements RssParser {
    @Override
    public List<RssItem> parse(InputStream is, Integer sourceId) {
        SyndFeed feed = RssUtil.buildSyndFeed(is);
        if (feed == null) {
            return null;
        }
        return feed.getEntries()
                .stream()
                .limit(50)
                .map(entry -> RssItem.builder()
                        .sourceId(sourceId)
                        .title(entry.getTitle().replaceAll("\\[", "").replaceAll("]", ""))
                        .link(entry.getLink())
                        .linkHash(HashUtil.sha256(entry.getLink()))
                        .pubDate(entry.getUpdatedDate())
                        .description(entry.getDescription() == null ? "" : entry.getDescription().getValue())
                        .build())
                .toList();
    }

}