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
 * @description rss xml文档链接
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rss_sources")
public class RssSource {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * RSS 订阅源 URL
     */
    @TableField("source_url")
    private String sourceUrl;

    /**
     * 订阅源名称
     */
    @TableField("source_name")
    private String sourceName;

    /**
     * 订阅源描述
     */
    @TableField("description")
    private String description;

    /**
     * 上次检查时间
     */
    @TableField("last_checked")
    private java.util.Date lastChecked;
}
