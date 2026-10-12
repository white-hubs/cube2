package com.guhecloud.platform.job.jobtask.core;

import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;

import java.util.Map;

/**
 * 调度器接口：定义任务调度的核心操作
 * <p>
 * 所有具体调度实现（如Quartz、XXL-JOB）都应实现此接口
 * 实现解耦，便于未来替换调度引擎
 * <p>
 * 2025-08-26
 */
public interface JobScheduler {

    /**
     * 添加一个新任务
     *
     * @param jobAddInfo 任务注册信息
     *
     * @return 任务名称
     */
    String addJob(JobAddInfo jobAddInfo);

    /**
     * 修改任务
     *
     * @param name  任务名 必填
     * @param cron  表达式 必填
     * @param desc  描述 为空不修改
     * @param param 参数 为空不修改
     *
     * @return true表示注册成功，false表示失败
     */
    boolean updateJob(String name, String cron, String desc, String param);

    /**
     * 启动一个任务
     *
     * @param name 任务名称
     *
     * @return true表示启动成功，false表示失败
     */
    boolean startJob(String name);

    /**
     * 启动所有任务
     *
     * @return true表示启动成功，false表示失败
     */
    boolean startJobs();

    /**
     * 触发执行一个任务(立即运行一次)
     *
     * @param name  任务name
     * @param param 执行参数（可为空）
     *
     * @return true表示触发成功，false表示失败
     */
    boolean runOnceJob(String name, String param);

    /**
     * 停止任务
     *
     * @param name 任务名称
     *
     * @return true表示停止成功，false表示失败（如任务不存在）
     */
    boolean stopJob(String name);

    /**
     * 删除任务
     *
     * @param name 任务名称
     *
     * @return true表示移除成功，false表示移除失败
     */
    boolean removeJob(String name);

    /**
     * 获取任务信息
     *
     * @param name 任务名称
     *
     * @return 任务信息
     */
    JobInfo getJob(String name);

    /**
     * 获取所有任务信息
     *
     * @return 任务信息映射表（任务名 -> JobInfo）
     */
    Map<String, JobInfo> getAllJobs();
}
