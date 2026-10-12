package com.guhecloud.cube.srv.gen.safety.management.report.service;

import com.guhecloud.cube.srv.gen.safety.management.report.param.AlarmSafetyReportParam;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmSafetyReportVO;
import com.guhecloud.platform.base.model.Pagination;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  11:40
 * @Description:
 * @Version: 1.0
 */
public interface AlarmSafetyReportService {
    Pagination<AlarmSafetyReportVO> securityReportPage(AlarmSafetyReportParam param);

    /**
     * 生成月报
     */
    void addSafetyReport(LocalDateTime reportDate, String response);

}
