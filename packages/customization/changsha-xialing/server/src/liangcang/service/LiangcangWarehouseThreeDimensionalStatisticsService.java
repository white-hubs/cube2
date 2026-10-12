package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseThreeDimensionalPeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalEnvironmentHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalMeasureHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalOverviewVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseOverviewSummaryVO;

import java.util.List;

/** 粮仓三维统计。 */
public interface LiangcangWarehouseThreeDimensionalStatisticsService {

    List<WarehouseThreeDimensionalOverviewVO> overview();

    WarehouseOverviewSummaryVO overviewSummary();

    List<WarehouseThreeDimensionalMeasureHistoryVO> measureHistory(WarehouseThreeDimensionalPeriodParam param);

    List<WarehouseThreeDimensionalEnvironmentHistoryVO> environmentHistory(WarehouseThreeDimensionalPeriodParam param);
}
