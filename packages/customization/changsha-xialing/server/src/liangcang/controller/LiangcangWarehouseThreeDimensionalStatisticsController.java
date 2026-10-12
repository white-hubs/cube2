package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseThreeDimensionalPeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.LiangcangWarehouseThreeDimensionalStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseOverviewSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalEnvironmentHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalMeasureHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 粮仓三维统计外部接口。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseThreeDimensionalStatisticsController {

    private final LiangcangWarehouseThreeDimensionalStatisticsService statisticsService;

    /**
     * 绑定粮仓总览。
     */
    @PostMapping("/liangcang/external/warehouse/three-dimensional/overview")
    public List<WarehouseThreeDimensionalOverviewVO> overview() {
        return statisticsService.overview();
    }

    /**
     * 绑定粮仓总揽。
     */
    @PostMapping("/liangcang/external/warehouse/three-dimensional/overview-summary")
    public WarehouseOverviewSummaryVO overviewSummary() {
        return statisticsService.overviewSummary();
    }

    /**
     * 粮仓测量信息历史。
     */
    @PostMapping("/liangcang/external/warehouse/three-dimensional/measure-history")
    public List<WarehouseThreeDimensionalMeasureHistoryVO> measureHistory(
            @RequestBody WarehouseThreeDimensionalPeriodParam param
                                                                         ) {
        return statisticsService.measureHistory(param);
    }

    /**
     * 温湿度气体历史。
     */
    @PostMapping("/liangcang/external/warehouse/three-dimensional/environment-history")
    public List<WarehouseThreeDimensionalEnvironmentHistoryVO> environmentHistory(
            @RequestBody WarehouseThreeDimensionalPeriodParam param
                                                                                 ) {
        return statisticsService.environmentHistory(param);
    }
}
