package me.koutian.common.task;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * quartz 配置类
 *
 * @author wuzhen
 */
@Configuration
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzAutoConfiguration {
    private final ApplicationContext applicationContext;

    public QuartzAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public BeanPostProcessor getBeanPostProcessor() {
        return new QuartzScheduledAnnotationBeanPostProcessor(applicationContext.getBean(Scheduler.class));
    }

    @Bean
    public Job getJob() {
        return new MethodInvokingJob(applicationContext);
    }
}