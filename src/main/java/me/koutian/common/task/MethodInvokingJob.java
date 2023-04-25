package me.koutian.common.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jujan
 */
public class MethodInvokingJob implements Job {

    private final ApplicationContext applicationContext;

    public MethodInvokingJob(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String targetBeanName = jobDataMap.getString("targetBeanName");
        String targetMethod = jobDataMap.getString("targetMethod");

        Object bean = applicationContext.getBean(targetBeanName);

        try {
            Method method = bean.getClass().getMethod(targetMethod);
            method.invoke(bean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new JobExecutionException(e);
        }
    }
}