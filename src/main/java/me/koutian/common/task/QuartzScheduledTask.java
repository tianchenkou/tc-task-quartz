package me.koutian.common.task;


/**
 * 基于Quartz的自动定时任务接口
 *
 * @author wuzhen
 * @date 2020/4/25
 */
public interface QuartzScheduledTask {
    /**
     * 执行定时任务
     */
    void execute();

    /**
     * 获取定时任务的cron表达式
     *
     * @return cron表达式
     */
    String getCronExpression();
}