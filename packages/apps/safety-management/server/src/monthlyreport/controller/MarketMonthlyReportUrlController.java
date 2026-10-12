package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportUrlPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportUrlService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketMonthlyReportUrlController {

    private final MarketMonthlyReportUrlService marketMonthlyReportUrlService;

    /**
     * 月报链接分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/market_month_report_url/page")
    public Pagination<MarketMonthlyReportUrlVO> getPage(@Valid MarketMonthlyReportUrlQuery query) {
        Page<MarketMonthlyReportUrlVO> page = this.marketMonthlyReportUrlService.getPage(query);
        return PagingUtils.convert(page);
    }

    /**
     * 月报链接状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping("/market_month_report_url/{id}/enabled/{enabled}")
    public void updateMarketMonthlyReportUrlEnabled(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled) {
        this.marketMonthlyReportUrlService.updateMarketMonthlyReportUrlEnabled(id, enabled);
    }

    /**
     * 月报链接详情
     *
     * @param id
     */
    @GetMapping("/public/market_month_report_url/{id}")
    public MarketMonthlyReportUrlVO getMarketMonthlyReportUrl(@PathVariable("id") Long id) {
        MarketMonthlyReportUrlPO marketMonthlyReportUrlPO = this.marketMonthlyReportUrlService.getById(id);
        Assert.isNotFound(marketMonthlyReportUrlPO, "月报链接不存在或已被删除");
        return ObjectUtils.copying(marketMonthlyReportUrlPO, MarketMonthlyReportUrlVO.class);
    }
}
