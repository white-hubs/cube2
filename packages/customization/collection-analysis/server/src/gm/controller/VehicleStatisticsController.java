package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.VehicleStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.VehicleTotalResult;
import com.guhecloud.cube.biz.collection.analysis.gm.service.VehicleStatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 光明小时统计
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleStatisticsController {
    private final VehicleStatisticsService vehicleStatisticsService;

    /**
     * 获取车辆统计
     *
     * @param param 入参条件
     *
     * @return 统计对象
     */
    @GetMapping("/gm/vehicle/stat/total")
    public VehicleTotalResult getPeriodTotal(@Valid VehicleStatisticsQueryParam param) {
        return vehicleStatisticsService.getPeriodTotal(param);
    }

    /**
     * 查询趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/gm/vehicle/stat/trend")
    public List<TrendResult> getTrend(VehicleStatisticsQueryParam param) {
        return vehicleStatisticsService.getTrend(param);
    }

    /**
     * 近一日车进场流分布图
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/gm/vehicle/stat/day-trend")
    public List<TrendResult> getDayTrend(VehicleStatisticsQueryParam param) {
        return vehicleStatisticsService.getDayTrend(param);
    }

    /**
     * 近一周车辆装货趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("gm/vehicle/stat/load-trend")
    public List<TrendResult> getLoadTrend(VehicleStatisticsQueryParam param) {
        return vehicleStatisticsService.getLoadTrend(param);
    }
}
