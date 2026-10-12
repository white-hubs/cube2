package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyWindUpPO;

import java.util.List;

public interface MarketMonthlyWindUpService extends IService<MarketMonthlyWindUpPO> {

    Long saveOrUpdate(MarketMonthlyReportPO marketMonthlyReportPO);

    List<MarketMonthlyReportVO> getWindUpMarketReportsByMonthlyReportId(Long monthlyReportId);
}
