package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendStatisticsVO;

import java.util.List;

/** 仓库粮温趋势统计。 */
public interface WarehouseTemperatureTrendStatisticsService {

    /** 特殊温度报表布局对应的仓房 ID，未配置时返回 null。 */
    Long specialLayoutWarehouseId();

    /** 查询仓库粮温趋势统计。 */
    WarehouseTemperatureTrendStatisticsVO statistics(WarehouseTemperatureTrendStatisticsParam param);

    /** 查询粮仓温度报表。 */
    List<List<WarehouseTemperatureReportPointVO>> temperatureReport(WarehouseTemperatureReportParam param);
}
