package com.guhecloud.platform.job.spring.impl;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.core.JobInfo;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 基于 Spring Task 的动态定时任务调度器
 * 支持添加、更新、启动、停止、移除、查询任务
 */
@Slf4j
public class SpringScheduler implements JobScheduler {

    private final TaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final Map<String, JobInfo> jobInfos = new ConcurrentHashMap<>();

    public SpringScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public synchronized String addJob(JobAddInfo jobAddInfo) {
        jobInfos.put(jobAddInfo.getName(), jobAddInfo.fromToJobInfo()); // 保存注册信息
        if (jobAddInfo.isEnabled()) {
            startJob(jobAddInfo.getName());
        }
        return jobAddInfo.getName();

    }

    @Override
    public synchronized boolean updateJob(String name, String cron, String desc, String param) {
        Assert.isNotFound(!jobInfos.containsKey(name), "job does not exist,job name:" + name);
        var jobInfo = jobInfos.get(name);
        var oldFuture = scheduledTasks.get(name);
        oldFuture.cancel(false);
        scheduledTasks.remove(name);
        if (StringUtils.isNotBlank(name)) {
            jobInfo.setName(name);
        }
        if (StringUtils.isNotBlank(cron)) {
            jobInfo.setCron(cron);
        }
        if (StringUtils.isNotBlank(desc)) {
            jobInfo.setDesc(desc);
        }
        if (StringUtils.isNotBlank(param)) {
            jobInfo.setParam(param);
        }
        return startJob(jobInfo.getName());
    }

    @Override
    public synchronized boolean startJob(String name) {
        if (scheduledTasks.containsKey(name)) {
            log.warn("任务 [{}] 已在运行，无需启动", name);
            return true;
        }
        var jobInfo = jobInfos.get(name);
        Assert.isNotFound(jobInfo, "job does not exist,job name:" + name);
        //  创建任务执行体
        Runnable task = () -> {
            try {
                // 使用 ReflectionUtils 安全调用方法
                ReflectionUtils.invokeMethod(jobInfo.getMethod(), jobInfo.getBean(), jobInfo.getParam());
            } catch (Exception e) {
                log.error("任务 [{}] 执行失败: {}", jobInfo.getName(), e.getMessage(), e);
            }
        };
        var trigger = new CronTrigger(jobInfo.getCron());
        var future = taskScheduler.schedule(task, trigger);
        if (future != null) {
            scheduledTasks.put(name, future);
            return true;
        } else {
            log.error("添加任务失败，调度器未返回 ScheduledFuture: {}", name);
            return false;
        }
    }

    /**
     * 启动所有任务(标记执行，会按表达式执行。并不是立即触发任务)
     *
     * @return true表示启动成功，false表示失败
     */
    @Override
    public synchronized boolean startJobs() {
        jobInfos.forEach((name, registration) -> {
            // 如果没有启动，并且是启用状态
            if (!scheduledTasks.containsKey(name)) {
                startJob(name);
            }
        });
        return true;
    }

    @Override
    public boolean runOnceJob(String name, String param) {
        var jobInfo = jobInfos.get(name);
        Assert.isNotFound(jobInfo, "job does not exist,job name:" + name);
        try {
            // 临时使用传入的 param 调用
            ReflectionUtils.invokeMethod(jobInfo.getMethod(), jobInfo.getBean(), param);
            return true;
        } catch (Exception e) {
            log.error("run once trigger job error,job name: [{}] error msg: {}", jobInfo.getName(), e.getMessage(), e);
            throw new ServiceException("run once trigger job error", e);
        }
    }

    @Override
    public synchronized boolean stopJob(String name) {
        var future = scheduledTasks.get(name);
        if (future == null) {
            return true;
        }
        future.cancel(false);
        scheduledTasks.remove(name);
        return true;
    }

    @Override
    public synchronized boolean removeJob(String name) {
        stopJob(name);
        jobInfos.remove(name);
        return true;
    }

    @Override
    public JobInfo getJob(String name) {
        return jobInfos.get(name);
    }

    @Override
    public Map<String, JobInfo> getAllJobs() {
        return Collections.unmodifiableMap(jobInfos);
    }
}