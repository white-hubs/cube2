package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectAreaStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 描述：能耗每日统计服务
 *
 * @author appet
 * @date 2021-12-30 15:12:36
 */
public interface EnergyDailyUsageProjectService extends IService<EnergyDailyUsageProject> {
    Pagination<EnergyDailyUsageProjectResultVO> getProjectPage(EnergyDailyUsageParam param);

    /**
     * 分项的时间趋势统计
     *
     * @param param
     *
     * @return
     */
    List<TrendStatVO> getProjectUsageStatForTrend(EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByTime(EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByCategory(EnergyTrendUsageParam param);

    /**
     * 这是一个定制的返回能耗流向 固定格式的数据
     *
     * @param param
     *
     * @return
     */
    Map<String, List<Map<String, String>>> getProjectUsageStatByTypeAndProject(EnergyTrendUsageParam param);

    EnergyProjectAreaStatVO getDailyUsageProjectStatAllOne(EnergyTrendUsageParam param);
}