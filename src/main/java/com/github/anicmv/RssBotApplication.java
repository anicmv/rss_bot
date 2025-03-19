package com.github.anicmv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description 启动类
 */
@SpringBootApplication
@MapperScan("com.github.anicmv.mapper")
@EnableScheduling
public class RssBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(RssBotApplication.class, args);
    }
}
