package com.guhecloud.platform.job.xxl.handler;

import com.guhecloud.platform.base.util.StringUtils;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 简单任务处理器
 * 作用：
 * - 统一处理日志记录、异常捕获等横切逻辑
 */
@Slf4j
public class SimpleJobHandler extends IJobHandler {

    private final Object targetBean;
    private final Method method;
    private final String param;
    private final String jobName;

    public SimpleJobHandler(Object targetBean, Method method, String param, String jobName) {
        this.targetBean = targetBean;
        this.method = method;
        this.param = param;
        this.jobName = jobName;
    }

    /**
     * 此方法会被 XXL-Job 调度中心调用
     * 日志、异常处理等通用逻辑
     */
    @Override
    public void execute() throws Exception {
        String paramValue = XxlJobContext.getXxlJobContext().getJobParam();
        try {
            // 执行实际的任务逻辑
            method.invoke(targetBean, StringUtils.isBlank(paramValue) ? param : paramValue);
            // 标记为成功
            XxlJobHelper.handleSuccess("SUCCESS");
        } catch (Exception e) {
            // 记录详细错误日志
            log.error("{}【任务执行失败】", jobName, e);
            // XXL-Job 记录任务状态为失败
            XxlJobHelper.handleFail("任务执行失败: " + e.getMessage());
            // 抛出异常
            var cause = e.getCause();
            throw new RuntimeException(cause);
        }
    }
}
