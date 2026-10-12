package com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.job;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.FlowFormDataCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.PersonnelAbility;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务处理
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobHandler {

    private final FlowFormDataCommandService flowFormDataCommandService;

    @JobAnnotation(name = "updateRegularWorkerJob", cron = "0 0 1 * * ?", desc = "人事-更新转正业务数据任务")
    public void updateRegularWorkerData(String param) {
        log.info("更新转正业务数据任务开始执行，额外参数:{}", param);
        if (PersonnelAbility.regularWorkerFlag()) {
            flowFormDataCommandService.jobUpdateRegularWorkerData();
        }
    }

    @JobAnnotation(name = "updateRotationPostJob", cron = "0 0 1 * * ?", desc = "人事-更新轮岗业务数据任务")
    public void updateRotationPostData(String param) {
        log.info("更新轮岗业务数据任务开始执行，额外参数:{}", param);
        flowFormDataCommandService.jobUpdateRotationPostData();
    }

    @JobAnnotation(name = "updateLeaveOfficeJob", cron = "0 0 2 * * ?", desc = "人事-更新离职业务数据任务开始执行")
    public void updateLeaveOfficeJob(String param) {
        log.info("更新离职业务数据任务开始执行，额外参数:{}", param);
        flowFormDataCommandService.jobUpdateLeaveOfficeData();
    }
}
