package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.MarketMonthlyReportUrlModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportUrlPO;

public interface MarketMonthlyReportUrlService extends IService<MarketMonthlyReportUrlPO> {

    Long save(MarketMonthlyReportUrlModifyRequest param);

    Page<MarketMonthlyReportUrlVO> getPage(MarketMonthlyReportUrlQuery query);

    void updateMarketMonthlyReportUrlEnabled(Long id, Boolean enabled);
}
