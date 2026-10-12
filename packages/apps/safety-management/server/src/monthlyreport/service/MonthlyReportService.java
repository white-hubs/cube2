package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MonthlyReportPO;
import com.guhecloud.platform.base.model.Pagination;

import java.time.LocalDateTime;

public interface MonthlyReportService extends IService<MonthlyReportPO> {

    Pagination<MonthlyReportVO> queryPage(MonthlyReportQuery query);

    Long createMonthlyReport(String title, String monthly, LocalDateTime monthDate);

}
