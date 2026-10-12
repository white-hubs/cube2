package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.job;

import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportCreateService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonthlyReportCreateJob {

    private final MonthlyReportCreateService monthlyReportCreateService;

    @JobAnnotation(name = "monthlyReportCreate", cron = "0 0 0 20 * ?", desc = "深农总部-深农安全月报生成")
    public void createMonthlyReport(String param) {
        this.monthlyReportCreateService.createMonthlyReport();
    }
}
