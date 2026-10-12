package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.platform.base.model.Pagination;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:45
 * @Description:
 * @Version: 1.0
 */
public interface MarketMonthlyReportService extends IService<MarketMonthlyReportPO> {

    Pagination<MarketMonthlyReportVO> queryByPage(MarketMonthlyReportQuery query);

    List<MarketMonthlyReportVO> getMarketReportsByMonthlyReportId(Long monthlyReportId);

    Boolean escalation(Long id);

    Long createMonthlyReport(String title, Long monthlyReportId, String monthly, LocalDateTime monthDate, MarketConfigPO marketConfig);

}
