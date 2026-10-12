package com.guhecloud.cube.srv.gen.safety.management.report.interfaces;

import com.guhecloud.cube.srv.gen.safety.management.report.param.AlarmSafetyReportParam;
import com.guhecloud.cube.srv.gen.safety.management.report.service.AlarmSafetyReportService;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmSafetyReportVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 安全报表（市场）
 *
 * @Author: lijie
 * @CreateTime: 2024-04-10  11:06
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmSafetyReportController {
    private final AlarmSafetyReportService alarmSafetyReportService;

    /**
     * 查询安全报告
     */
    @GetMapping("/alarm/securityReportPage")
    public Pagination<AlarmSafetyReportVO> securityReportPage(AlarmSafetyReportParam param) {
        return alarmSafetyReportService.securityReportPage(param);
    }

    @GetMapping("/alarm/securityReportExport")
    public void securityReportExport(HttpServletResponse response, AlarmSafetyReportParam param) {
        alarmSafetyReportService.addSafetyReport(LocalDateTime.now(), null);
    }

}
