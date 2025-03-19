package com.github.anicmv.entity;

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
 * @description telegram user info
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {

    /**
     * Telegram 用户 ID
     */
    @TableId("telegram_id")
    private Long telegramId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private java.util.Date createdAt;
}
