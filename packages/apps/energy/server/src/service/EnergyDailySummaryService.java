package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailySummary;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailySummaryResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface EnergyDailySummaryService extends IService<EnergyDailySummary> {
    Pagination<EnergyDailySummaryResultVO> getSummaryPage(EnergyDailyUsageParam param);

    List<TrendStatVO> getDailySummaryTrend(EnergyTrendUsageParam param);
}
