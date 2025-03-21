package com.github.anicmv.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description 用户订阅信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_subscriptions")
public class UserSubscription {
    /**
     * Telegram 用户 ID
     */
    @TableId("telegram_id")
    private Long telegramId;

    /**
     * 订阅源表主键
     */
    @TableField("source_id")
    private Integer sourceId;

    /**
     * 订阅时间
     */
    @TableField("subscribed_at")
    private Date subscribedAt;

    /**
     * 最后通知时间
     */
    @TableField("notified_at")
    private Date notifiedAt;
}
