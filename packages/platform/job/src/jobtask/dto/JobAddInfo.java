package com.guhecloud.platform.job.jobtask.dto;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.job.jobtask.core.JobInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * job任务信息
 */
@Data
@NoArgsConstructor
public class JobAddInfo {
    private String name;        // 任务名称
    private String cron;        // 执行周期（Cron表达式）
    private String desc;        // 任务描述
    private String param;        // 任务参数
    private Object bean;        // 目标Spring Bean实例
    private Method method;      // 要执行的方法
    private boolean enabled;    // 是否启用

    public JobAddInfo(Object bean, Method method, String name, String cron, String desc, String param, Boolean enabled) {
        this.bean = bean;
        this.method = method;
        this.name = name;
        this.cron = cron;
        this.param = param;
        this.desc = desc;
        this.enabled = Boolean.TRUE.equals(enabled);
        ReflectionUtils.makeAccessible(method);
    }

    public JobAddInfo(Object bean, String methodName, String name, String cron, String desc, String param, Boolean enabled) {
        Assert.isParameterError(StringUtils.isBlank(name), "任务名不能为空");
        Method beanMethod;
        try {
            beanMethod = bean.getClass().getDeclaredMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.bean = bean;
        this.method = beanMethod;
        this.name = name;
        this.cron = cron;
        this.param = param;
        this.desc = desc;
        this.enabled = Boolean.TRUE.equals(enabled);
        ReflectionUtils.makeAccessible(method);
    }

    /**
     * 转换为job对象
     *
     * @return JobInfo
     */
    public JobInfo fromToJobInfo() {
        return new JobInfo(this.name,
                           null,
                           cron,
                           desc,
                           param,
                           bean,
                           method);
    }

    /**
     * 从注解和对象方法构建任务注册对象
     *
     * @param bean       Spring容器中的Bean实例
     * @param method     要执行的方法对象
     * @param annotation 注解实例
     *
     * @return 构建好的 Job任务 对象
     */
    public static JobAddInfo fromAnnotation(Object bean, Method method, JobAnnotation annotation) {
        var reg = new JobAddInfo();
        reg.name = annotation.name();
        reg.cron = annotation.cron();
        reg.desc = annotation.desc();
        reg.param = annotation.param();
        reg.bean = bean;
        reg.method = method;
        reg.enabled = annotation.enabled();
        return reg;
    }
}
