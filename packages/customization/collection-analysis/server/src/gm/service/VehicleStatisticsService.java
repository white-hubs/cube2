package com.guhecloud.cube.biz.collection.analysis.gm.service;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.VehicleStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.VehicleTotalResult;

import java.util.List;

public interface VehicleStatisticsService {
    VehicleTotalResult getPeriodTotal(VehicleStatisticsQueryParam param);

    List<TrendResult> getTrend(VehicleStatisticsQueryParam param);

    List<TrendResult> getDayTrend(VehicleStatisticsQueryParam param);

    List<TrendResult> getLoadTrend(VehicleStatisticsQueryParam param);
}
