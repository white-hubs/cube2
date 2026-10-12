package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 粮仓温度统计接口。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseTemperatureStatisticsController {

    private final WarehouseTemperatureTrendStatisticsService warehouseTemperatureTrendStatisticsService;

    /** 查询仓库粮温趋势统计。 */
    @PostMapping("/liangcang/warehouse-management/temperature-trend/statistics")
    public WarehouseTemperatureTrendStatisticsVO statistics(@RequestBody WarehouseTemperatureTrendStatisticsParam param) {
        return warehouseTemperatureTrendStatisticsService.statistics(param);
    }

    /** 查询粮仓温度报表。 */
    @PostMapping("/liangcang/warehouse-management/temperature-report/list")
    public List<List<WarehouseTemperatureReportPointVO>> temperatureReport(@RequestBody WarehouseTemperatureReportParam param) {
        return warehouseTemperatureTrendStatisticsService.temperatureReport(param);
    }
}
