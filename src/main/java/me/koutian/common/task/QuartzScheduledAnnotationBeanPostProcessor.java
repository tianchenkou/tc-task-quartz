package me.koutian.common.task;

import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jujan
 */
public class QuartzScheduledAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Scheduler scheduler;

    public QuartzScheduledAnnotationBeanPostProcessor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            QuartzScheduled quartzScheduled = AnnotationUtils.findAnnotation(method, QuartzScheduled.class);
            if (quartzScheduled != null) {
                // 获取cron表达式
                String cron = quartzScheduled.cron();
                // 创建JobDetail
                JobDetail jobDetail = JobBuilder.newJob(MethodInvokingJob.class)
                        .withIdentity(beanName + "_" + method.getName())
                        .build();
                // 设置JobDataMap
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                jobDataMap.put("targetBeanName", beanName);
                jobDataMap.put("targetMethod", method.getName());
                // 创建Trigger
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .withIdentity(beanName + "_" + method.getName() + "_trigger")
                        .build();
                Set<Trigger> triggers = new HashSet<>();
                triggers.add(trigger);
                // 调度任务
                try {
                    scheduler.scheduleJob(jobDetail, triggers, true);
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}