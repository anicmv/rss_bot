package com.github.anicmv.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author anicmv
 * @date 2024/9/19 15:39
 * @description 全局配置
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class GlobalConfig {
    private String token;
    private String cron;
    private String username;
    private String retry;
}
