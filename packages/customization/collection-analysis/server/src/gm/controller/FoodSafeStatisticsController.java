package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.FoodSafeStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.FoodSafeStatisticsResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.service.FoodSafeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 食安统计
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodSafeStatisticsController {
    private final FoodSafeStatisticsService foodSafeStatisticsService;

    /**
     * 获取某日的统计
     *
     * @param day 日期(yyyy-MM-dd)
     *
     * @return 某天的统计
     */
    @GetMapping("/gm/food-safe/stat/day")
    public FoodSafeStatisticsResult getStatByDay(String day) {
        return foodSafeStatisticsService.getStatByDay(day);
    }

    /**
     * 获取检测次数趋势
     *
     * @param param 条件
     *              format 格式时间化格式
     *              period 时间区间
     *
     * @return 趋势
     */
    @GetMapping("/gm/food-safe/stat/trend")
    public List<TrendResult> getTrend(FoodSafeStatisticsQueryParam param) {
        return foodSafeStatisticsService.getTrend(param);
    }

    @GetMapping("/gm/food-safe/stat/ranking")
    public List<TrendResult> getRanking(@RequestParam("day") String day) {
        return foodSafeStatisticsService.getRanking(day);
    }
}
