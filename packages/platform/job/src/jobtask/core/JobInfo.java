package com.guhecloud.platform.job.jobtask.core;

import com.guhecloud.platform.job.jobtask.enums.JobStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * 任务信息类：用于展示任务状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInfo {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务状态
     */
    private JobStatusEnum status;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 目标Spring Bean实例
     */
    private Object bean;

    /**
     * 要执行的方法
     */
    private Method method;
}


