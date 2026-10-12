package com.guhecloud.cube.base.flow.flowable.job;

import com.guhecloud.cube.base.flow.context.querygateway.OverdueAutomaticApprovalService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-16  16:18
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OverdueAutomaticApprovalJob {
    private final OverdueAutomaticApprovalService overdueAutomaticApproval;

    /**
     * 定时任务：自动审批过期流程
     *
     * @param param 流程相关参数
     *
     * @return 返回一个ReturnT类型的对象，表示任务执行结果
     */
    @JobAnnotation(name = "checkOverdueTasks", cron = "0 0/5 * * * ?", desc = "流引擎-审批超时处理")
    public void checkOverdueTasks(String param) {
        SessionContextHolder.clearSessionContext();
        overdueAutomaticApproval.checkOverdueTasks();
        SessionContextHolder.clearSessionContext();
        log.info("检查需要自动审批通过流程结束");
    }
}
