package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailySummaryMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailySummary;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailySummaryService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.TimeStatUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailySummaryResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日用能汇总统计
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyDailySummaryServiceImpl extends ServiceImpl<EnergyDailySummaryMapper, EnergyDailySummary> implements EnergyDailySummaryService {

    private final EnergyDailySummaryMapper energyDailySummaryMapper;

    /**
     * 汇总分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<EnergyDailySummaryResultVO> getSummaryPage(EnergyDailyUsageParam param) {
        Period period = param.getPeriod();
        Page<EnergyDailySummaryResultVO> page = energyDailySummaryMapper.getSummaryPage(PagingUtils.convert(param), Wrappers
                                                                                                .<EnergyDailySummary>lambdaQuery()
                                                                                                .between(period !=
                                                                                                         null, EnergyDailySummary::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                                .orderByDesc(EnergyDailySummary::getStatDate)
                                                                                       );
        return PagingUtils.convert(page);
    }

    /**
     * 汇总趋势
     *
     * @param param 条件
     *
     * @return 汇总趋势
     */
    @Override
    public List<TrendStatVO> getDailySummaryTrend(EnergyTrendUsageParam param) {
        //初始化时间
        TimeStatUtil.initStatParam(param);
        return energyDailySummaryMapper.getDailySummaryForTrend(param.getPeriod());
    }
}
