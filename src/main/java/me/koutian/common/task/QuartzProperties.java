package me.koutian.common.task;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * quartz 配置属性类
 *
 * @author wuzhen
 */
@ConfigurationProperties(prefix = "quartz")
public class QuartzProperties {
    private String triggerName;
    private String cronExpression;
}