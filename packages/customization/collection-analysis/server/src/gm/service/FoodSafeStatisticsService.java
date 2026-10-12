package com.guhecloud.cube.biz.collection.analysis.gm.service;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.FoodSafeStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.FoodSafeStatisticsResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;

import java.util.List;

public interface FoodSafeStatisticsService {
    FoodSafeStatisticsResult getStatByDay(String day);

    List<TrendResult> getTrend(FoodSafeStatisticsQueryParam param);

    List<TrendResult> getRanking(String day);
}
