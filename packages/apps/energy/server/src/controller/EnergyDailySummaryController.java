package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailySummaryService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailySummaryResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 每日汇总统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyDailySummaryController {

    private final EnergyDailySummaryService energyDailySummaryService;

    /**
     * 汇总分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/energy-daily-summary/summaryPage")
    public Pagination<EnergyDailySummaryResultVO> getSummaryPage(EnergyDailyUsageParam param) {
        return energyDailySummaryService.getSummaryPage(param);
    }

    /**
     * 汇总趋势
     *
     * @param param 条件
     *
     * @return 汇总趋势
     */
    @GetMapping("/energy-daily-summary/trend")
    public List<TrendStatVO> getDailySummaryTrend(EnergyTrendUsageParam param) {
        return energyDailySummaryService.getDailySummaryTrend(param);
    }
}
