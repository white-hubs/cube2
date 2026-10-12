package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.DosageAnalysisParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.CarbonQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAnalysisQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataTimeFlagVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisDistributionVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAnalysisMenuVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyConsumptionDetailsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.model.Pagination;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface EnergyService {

    //综合能耗-数据统计-本月
    Map<String, Object> getComprehensiveEnergy();

    //能耗趋势
    Map<String, Object> getEnergyTrend();

    //用能趋势-近30日
    Map<String, List<DataTimeFlagVo>> energyConsumptionTrend();

    //用电排行-近30日
    List<AreaStatisticsVo> electricityConsumptionRanking();

    //能源走向-近30日
    Map<String, Object> energyDirection();

    //碳排统计
    Map<String, Object> carbonEmissionStatistics(Boolean isCarbon);

    //碳排趋势
    List<DataTimeFlagVo> carbonEmissionTrends(CarbonQueryParam carbonQueryParam);

    //碳排分项占比
    List<DataTimeFlagVo> proportionCarbonEmissionsItems(CarbonQueryParam carbonQueryParam);

    //分项
    Map<String, Object> getEnergyProject(EnergyProjectQueryParam param);

    //用量分析
    Map<String, Object> dosageAnalysis(DosageAnalysisParam param);

    //用能分析
    List<DosageAnalysisVo> energyAnalysis(DosageAnalysisParam param);

    //分布
    List<DosageAnalysisDistributionVo> energyAnalysisDistribution(DosageAnalysisParam param);

    //能耗排名
    List<DosageAnalysisVo> energyConsumptionRanking(DosageAnalysisParam param);

    List<EnergyConsumptionDetailsVo> getEnergyConsumptionDetails(DosageAnalysisParam param);

    //区域
    Map<String, Object> getEnergyArea(EnergyTrendParam param);

    List<EnergyAnalysisMenuVo> getEnergyAnalysisMenu();

    List<EnergyAnalysisMenuVo> getAppEnergyAnalysisMenu();

    Map<String, Object> getEnergyAnalysisOverview(EnergyAnalysisQueryParam param) throws ParseException;

    List<DataTimeFlagVo> getEnergyAnalysisTrend(EnergyTrendParam param) throws ParseException;

    Page<DataVo> EnergyAnalysisHistory(EnergyAnalysisQueryParam param) throws ParseException;

    Page<DataVo> EnergyAnalysisAppHistory(EnergyAnalysisQueryParam param) throws ParseException;

    /**
     * 设备趋势
     *
     * @param param diviceId 设备Id
     *
     * @return 趋势
     */
    List<TrendResultVO> getDeviceTrend(EnergyTrendUsageParam param);

    Pagination<KeyValueDTO> getDevicePeriodUseRanking(EnergyTrendUsageParam param);
}
