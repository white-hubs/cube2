package com.guhecloud.cube.srv.gen.maintain.server.upkeep.timetask;

import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceTasksCommandService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MaintenanceTasksJob {
    @Autowired
    private MaintenanceTasksCommandService maintenanceTasksCommandService;

    //计划任务生成
    @JobAnnotation(name = "maintenanceTask", cron = "0 0 0/1 * * ?", desc = "维保作业-维保计划生成任务")
    public void addPlanTask(String param) {
        try {
            maintenanceTasksCommandService.saveTask();
        } catch (Exception e) {
            log.error("维保检测失败,失败原因:", e);
        }
    }

    //计划失效
    @JobAnnotation(name = "maintenancePlanInvalid", cron = "0 1 0/1 * * ?", desc = "维保作业-维保计划失效")
    public void updatePlan(String param) {
        try {
            maintenanceTasksCommandService.updatePlan();
        } catch (Exception e) {
            log.error("维保失效监测失败,失败原因:", e);
        }
    }
}
