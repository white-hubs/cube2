package com.guhecloud.platform.job.jobtask.service;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.core.JobInfo;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * job服务
 * 上层调用者只依赖此工具类，无需关心底层是哪种调度器
 */
@Slf4j
@Component
public class JobService {
    @Autowired
    private JobScheduler jobScheduler; // 自动注入当前激活的调度器

    /**
     * 添加任务
     *
     * @param name       任务名(唯一)
     * @param bean       执行的类
     * @param methodName 执行的方法名
     * @param cron       cron表达式
     * @param desc       描述
     * @param param      参数
     *
     * @return 是否成功
     */
    public String addJob(String name, Object bean, String methodName,
                         String cron, String desc, String param, Boolean enabled
                        ) {
        var jobAddInfo = new JobAddInfo(bean, methodName, name, cron, desc, param, Boolean.TRUE.equals(enabled));
        return jobScheduler.addJob(jobAddInfo);
    }

    /**
     * 启动一个任务(标记执行，会按表达式执行。并不是立即触发任务)
     *
     * @param name 任务名称
     *
     * @return true表示启动成功，false表示失败
     */
    public boolean startJob(String name) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名称不能为空");
        return jobScheduler.startJob(name);
    }

    /**
     * 停止一个已经启动的任务(直到再次启动前都不会再执行)
     *
     * @param name 任务名称
     *
     * @return true表示停止成功，false表示失败（如任务不存在）
     */
    public boolean stopJob(String name) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名称不能为空");
        return jobScheduler.stopJob(name);
    }

    /**
     * 触发执行一个任务(立即运行一次)
     *
     * @param name  任务id
     * @param param 执行参数（可为空）
     *
     * @return true表示触发成功，false表示失败
     */
    public boolean runOnceJob(String name, String param) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名称不能为空");
        return jobScheduler.runOnceJob(name, param);
    }

    /**
     * 删除任务
     *
     * @param name 任务名称
     *
     * @return true表示移除成功，false表示移除失败
     */
    public boolean removeJob(String name) {
        if (StringUtils.isEmpty(name)) {
            log.warn("remove job task name is empty");
            return true;
        }
        return jobScheduler.removeJob(name);
    }

    /**
     * 修改任务
     *
     * @param name    任务名 必填
     * @param newCron 表达式 必填
     * @param desc    描述 为空不修改
     * @param param   参数 为空不修改
     *
     * @return true表示注册成功，false表示失败
     */
    public boolean updateJob(String name, String newCron, String desc,
                             String param
                            ) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名称不能为空！");
        if (StringUtils.isBlank(newCron) && StringUtils.isBlank(desc) && StringUtils.isBlank(param)) {
            log.warn("无修改");
            return true;
        }
        Assert.isParameterError(!CronExpression.isValidExpression(newCron), "无效的表达式！");
        return jobScheduler.updateJob(name, newCron, desc, param);
    }

    /**
     * 获取一个任务信息
     *
     * @param name 任务名id
     *
     * @return 任务信息
     */
    public JobInfo getJob(String name) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名称不能为空");
        return jobScheduler.getJob(name);
    }

    /**
     * 获取所有已注册任务的详细信息
     *
     * @return 任务信息映射表（任务名 -> JobInfo）
     */
    public Map<String, JobInfo> getAllJobs() {return jobScheduler.getAllJobs();}
}
