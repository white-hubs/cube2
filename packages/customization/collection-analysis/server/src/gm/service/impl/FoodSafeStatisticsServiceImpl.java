package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.FoodSafeStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.FoodSafeStatisticsResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.FoodSafeRankingStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.FoodSafeWeekStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.GmFoodSafeDayStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeDayStatisticsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeRankingStatisticsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeWeekStatisticsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.FoodSafeWeekRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.service.FoodSafeStatisticsService;
import com.guhecloud.cube.biz.collection.analysis.gm.utils.times.TrendTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食安统计
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodSafeStatisticsServiceImpl implements FoodSafeStatisticsService {

    private final GmFoodSafeDayStatisticsMapper gmFoodSafeDayStatisticsMapper;
    private final FoodSafeWeekStatisticsMapper foodSafeWeekStatisticsMapper;
    private final FoodSafeRankingStatisticsMapper foodSafeRankingStatisticsMapper;
    private final FoodSafeWeekRepository foodSafeWeekRepository;

    /**
     * 获取某日的统计
     *
     * @param day 日期(yyyy-MM-dd)
     *
     * @return 某天的统计
     */
    @Override
    public FoodSafeStatisticsResult getStatByDay(String day) {
        FoodSafeStatisticsResult result = new FoodSafeStatisticsResult();
        if (StringUtils.isBlank(day)) {
            return result;
        }
        FoodSafeDayStatisticsPO po = gmFoodSafeDayStatisticsMapper.selectOne(Wrappers.<FoodSafeDayStatisticsPO>lambdaQuery()
                                                                                     .eq(FoodSafeDayStatisticsPO::getTimeDayFormat, day)
                                                                                     .last(" limit 1 ")
                                                                            );
        if (po != null) {
            BeanUtils.copyProperties(po, result);
        }
        return result;
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
    @Override
    public List<TrendResult> getTrend(FoodSafeStatisticsQueryParam param) {
        List<FoodSafeWeekStatisticsPO> poList = foodSafeWeekRepository.getPOListByThird();
        if (!CollectionUtils.isEmpty(poList)) {
            return poList.stream()
                    .map(po -> TrendResult.builder().trendX(po.getTesteddate()).trendY(String.valueOf(po.getTestcount())).build())
                    .collect(Collectors.toList());
        }
        TrendTimeUtils.setFormatAndPeriod(param);
        log.warn("三方食安获取失败，获取本地数据！");
        return foodSafeWeekStatisticsMapper.selectTrend(param);
    }

    @Override
    public List<TrendResult> getRanking(String day) {
        List<TrendResult> result = new ArrayList<>();
        List<FoodSafeRankingStatisticsPO> poList = foodSafeRankingStatisticsMapper.selectList(Wrappers
                                                                                                      .<FoodSafeRankingStatisticsPO>lambdaQuery()
                                                                                                      .eq(FoodSafeRankingStatisticsPO::getTimeDayFormat, day)
                                                                                                      .orderByDesc(FoodSafeRankingStatisticsPO::getFailCount)
                                                                                             );
        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }

        return poList.stream().map(po -> TrendResult.builder().trendX(po.getSampleName()).trendY(String.valueOf(po.getFailCount())).build())
                .collect(Collectors.toList());
    }

}
