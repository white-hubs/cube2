package com.guhecloud.cube.srv.gen.safety.management.report.job;

import com.guhecloud.cube.srv.gen.safety.management.report.service.AlarmSafetyReportService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  19:18
 * @Description: 生成安全报告
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyReportJob {
    private final AlarmSafetyReportService alarmSafetyReportService;

    /**
     * 生成安全报告
     */
    @JobAnnotation(name = "addSafetyReport", cron = "0 0 0 1 * ?", desc = "商品菜价-从mdata同步档位数据", param = "安全指挥调度系统")
    public void addSafetyReport(String param) {
        log.info("生成安全报告定时任务开始执行");
        SessionContextHolder.clearSessionContext();
        alarmSafetyReportService.addSafetyReport(LocalDateTime.now(), param);
        log.info("生成安全报告定时任务执行结束");
        SessionContextHolder.clearSessionContext();
    }
}
