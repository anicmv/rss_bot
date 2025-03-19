package com.github.anicmv.config;

import com.github.anicmv.service.RssService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author anicmv
 * @date 2024/7/23 14:30
 * @description 定时配置
 */
@Log4j2
@Component
public class SchedulingConfig implements SchedulingConfigurer {

    private final GlobalConfig globalConfig;

    private final RssService rssService;

    public SchedulingConfig(RssService rssService, GlobalConfig globalConfig) {
        this.rssService = rssService;
        this.globalConfig = globalConfig;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(rssService::pushMessages, triggerContext -> {
            Trigger trigger = new CronTrigger(globalConfig.getCron());
            return trigger.nextExecution(triggerContext);
        });
    }
}