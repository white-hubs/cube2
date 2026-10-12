package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyRingRatioQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageRingRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyUsageStatWithQoqVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * 描述：能耗每日统计服务
 *
 * @author appet
 * @date 2021-12-30 15:12:36
 */
public interface EnergyDailyUsageService extends IService<EnergyDailyUsage> {

    Pagination<EnergyDailyUsageResultVO> getTypePage(EnergyDailyUsageParam param);

    /**
     * 分类用量统计
     *
     * @param param
     *
     * @return
     */
    Pagination<EnergyDailyUsageVO> getPageForCategory(EnergyDailyUsageParam param);

    /**
     * 分项用量统计
     *
     * @param param
     *
     * @return
     */
    Pagination<EnergyDailyUsageProjectVO> getPageForProject(EnergyDailyUsageParam param);

    /**
     * 批量保存和更新每日的用量
     *
     * @param dataList
     */
    void batchSaveOrUpdateEnergyDailyUsage(List<EnergyDailyUsage> dataList);

    /**
     * 根据能耗类型，统计年月日用量
     * 以及环比情况
     *
     * @param param
     *
     * @return
     */
    EnergyDailyUsageStatVO getUsageStatForDMY(EnergyDailyUsageParam param);

    EnergyDailyUsageStatVO getUsageStatWithCarbonForDMY(EnergyDailyUsageParam param);

    /**
     * 碳排按时间统计
     *
     * @param param
     *
     * @return
     */
    List<TrendStatVO> getUsageToDcStatForTrend(EnergyTrendUsageParam param);

    List<TrendStatVO> getUsageToDcStatByTimeAndType(EnergyTrendUsageParam param);

    List<EnergyUsageStatWithQoqVO> getUsageStatWithCarbonByTypeForAll(EnergyTrendUsageParam param);

    EnergyTypeStatVO getDailyUsageStatType(EnergyDailyUsageStatParam param);

    List<EnergyTypeStatVO> getDailyUsageStatOn(EnergyDailyUsageStatParam param);

    EnergyTypeStatVO getDailyUsageStatAllOn(EnergyDailyUsageStatParam param);

    EnergyDailyUsageRingRatioVO getDailyUsageRingRatio(EnergyRingRatioQueryParam param);

    List<EnergyTypeStatVO> getDailyUsageStatType(Period period, List<String> instrumentTypeList);
}