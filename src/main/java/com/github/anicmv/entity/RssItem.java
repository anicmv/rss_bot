package com.github.anicmv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description 链接条目实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rss_items")
public class RssItem {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * RSS 订阅源 id
     */
    @TableField("source_id")
    private Integer sourceId;

    /**
     * 消息标题
     */
    @TableField("title")
    private String title;

    /**
     * 消息链接
     */
    @TableField("link")
    private String link;


    @TableField("link_hash")
    private String linkHash;

    /**
     * 发布时间
     */
    @TableField("pub_date")
    private java.util.Date pubDate;

    /**
     * 消息描述
     */
    @TableField("description")
    private String description;
}
