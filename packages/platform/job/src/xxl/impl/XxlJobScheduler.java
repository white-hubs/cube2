package com.guhecloud.platform.job.xxl.impl;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.core.JobInfo;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import com.guhecloud.platform.job.jobtask.enums.JobStatusEnum;
import com.guhecloud.platform.job.xxl.dto.XxlJobInfo;
import com.guhecloud.platform.job.xxl.handler.SimpleJobHandler;
import com.guhecloud.platform.job.xxl.operator.XxlJobApiOperator;
import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * xxljob 实现
 */
@Slf4j
public class XxlJobScheduler implements JobScheduler {

    private final Map<String, JobInfo> jobInfos = new ConcurrentHashMap<>();

    /**
     * 添加一个新任务
     *
     * @param jobAddInfo 任务信息
     *
     * @return true表示注册成功，false表示失败（如名称冲突）
     */
    @Override
    public synchronized String addJob(JobAddInfo jobAddInfo) {
        var jobName = jobAddInfo.getName();  // 任务名称（Handler 名称）
        // 创建适配器
        var handler = new SimpleJobHandler(jobAddInfo.getBean(), jobAddInfo.getMethod(), jobAddInfo.getParam(), jobAddInfo.getName());
        var jobExist = jobInfos.values().stream().anyMatch(job -> job.getName().equals(jobAddInfo.getName()));
        Assert.isConflict(jobExist, "job name already exists");
        try {
            removeJob(jobName);
            var dto = XxlJobInfo.registrationToXxlJobInfo(jobAddInfo);
            XxlJobApiOperator.addJob(dto);
            XxlJobExecutor.registJobHandler(dto.getExecutorHandler(), handler);
            jobInfos.put(jobAddInfo.getName(), jobAddInfo.fromToJobInfo());
            if (jobAddInfo.isEnabled()) {
                startJob(jobAddInfo.getName());
            }
            return jobAddInfo.getName();
        } catch (Exception e) {
            throw new RuntimeException("注册 XXL-Job 任务失败: " + jobName, e);
        }
    }

    /**
     * 修改任务
     *
     * @param name  任务名
     * @param cron  表达式
     * @param desc  描述
     * @param param 参数
     *
     * @return true表示注册成功，false表示失败
     */
    @Override
    public synchronized boolean updateJob(String name, String cron, String desc, String param) {
        var xxlJobInfos = getXxlJobByExecutorHandler(name);
        var jobInfo = jobInfos.get(name);
        var jobExist = CollectionUtils.isEmpty(xxlJobInfos) || jobInfo == null;
        Assert.isNotFound(jobExist, "job does not exist,job name:" + name);
        xxlJobInfos.forEach(xxlJobInfo -> {
            if (StringUtils.isNotBlank(cron)) {
                xxlJobInfo.setScheduleConf(cron);
                jobInfo.setCron(cron);
            }
            if (StringUtils.isNotBlank(desc)) {
                xxlJobInfo.setJobDesc(desc);
                jobInfo.setDesc(desc);
            }
            if (StringUtils.isNotBlank(param)) {
                xxlJobInfo.setExecutorParam(param);
                jobInfo.setParam(param);
            }
            XxlJobApiOperator.updateJob(xxlJobInfo);
        });
        return true;
    }

    /**
     * 启动一个任务
     *
     * @param name 任务名称
     *
     * @return true表示触发成功，false表示失败
     */
    @Override
    public synchronized boolean startJob(String name) {
        List<XxlJobInfo> xxlJobByExecutorHandler = getXxlJobByExecutorHandler(name);
        xxlJobByExecutorHandler.forEach(job -> XxlJobApiOperator.startJob(job.getId().toString()));
        jobInfos.get(name).setStatus(JobStatusEnum.STARTED);
        return true;
    }

    /**
     * 启动所有任务(标记执行，会按表达式执行。并不是立即触发任务)
     *
     * @return true表示启动成功，false表示失败
     */
    @Override
    public synchronized boolean startJobs() {
        jobInfos.keySet().forEach(this::startJob);
        return true;
    }

    /**
     * 触发执行一个任务（支持传参）
     *
     * @param name  任务名称
     * @param param 执行参数（可选）
     *
     * @return true表示触发成功，false表示失败
     */
    @Override
    public boolean runOnceJob(String name, String param) {
        XxlJobApiOperator.getJobList(name)
                .stream().filter(job -> job.getExecutorHandler().equals(name))
                .forEach(job -> XxlJobApiOperator.triggerJob(job.getId().toString(), param));
        return true;
    }

    /**
     * 停止一个正在运行的任务
     *
     * @param name 任务名称
     *
     * @return true表示停止成功，false表示失败（如任务不存在）
     */
    @Override
    public synchronized boolean stopJob(String name) {
        var dto = getXxlJobByExecutorHandler(name);
        if (CollectionUtils.isEmpty(dto)) {
            return false;
        }
        dto.forEach(job -> XxlJobApiOperator.stopJob(job.getId().toString()));
        jobInfos.get(name).setStatus(JobStatusEnum.STOPPED);
        return false;
    }

    /**
     * 移除注册的任务
     *
     * @param name 任务名称
     *
     * @return true表示移除成功，false表示移除失败
     */
    @Override
    public synchronized boolean removeJob(String name) {
        var dto = getXxlJobByExecutorHandler(name);
        if (CollectionUtils.isEmpty(dto)) {
            return false;
        }
        dto.forEach(job -> XxlJobApiOperator.removeJob(job.getId().toString()));
        jobInfos.remove(name);
        return false;
    }

    /**
     * 获取任务信息
     *
     * @param name 任务名称
     *
     * @return 任务信息
     */
    @Override
    public JobInfo getJob(String name) {
        var dto = XxlJobApiOperator.getJobList(name)
                .stream().filter(job -> job.getExecutorHandler().equals(name))
                .collect(Collectors.toSet());
        Assert.isNotFound(CollectionUtils.isEmpty(dto), "ob does not exist,job name: " + name);
        return jobInfos.get(name);
    }

    /**
     * 获取所有已注册任务的详细信息
     *
     * @return 任务信息映射表（任务名 -> JobInfo）
     */
    @Override
    public Map<String, JobInfo> getAllJobs() {
        return Collections.unmodifiableMap(jobInfos);
    }

    public List<XxlJobInfo> getXxlJobByExecutorHandler(String name) {
        var dto = XxlJobApiOperator.getJobList(name)
                .stream().filter(job -> job.getExecutorHandler().equals(name))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dto)) {
            log.warn("job does not exist,job name: {}", name);
        }
        return dto;
    }
}
