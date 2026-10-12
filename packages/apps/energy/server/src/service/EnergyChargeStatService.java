package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ChargeComprehensiveParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ChargeQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeStatVo;

import java.util.List;
import java.util.Map;

public interface EnergyChargeStatService {

    List<EnergyChargeStatVo> getAreaDetailChargeByTime(String period, String instrumentType, String timeType, List<Long> areaIds);

    Map<String, List<EnergyChargeStatVo>> getChargeForFPGTrendByTime(ChargeQueryParam param);

    /**
     * 按能源类型计算费用
     *
     * @return
     */
    List<EnergyChargeStatVo> getChargeByType(ChargeQueryParam param);

    /**
     * @Description 综合费用
     * @Author lrj
     */
    Map<String, Object> getComprehensiveExpenses(ChargeComprehensiveParam param);

    /**
     * @Description 总费用趋势
     * @Author lrj
     */
    Map<String, List<ChargeStatisticsVo>> getComprehensiveExpensesTrend(ChargeComprehensiveParam param) throws Exception;

    EnergyChargeStatVo getAreaComprehensiveExpenses(ChargeComprehensiveParam param);

    List<ChargeStatisticsVo> getAreaOrRouteComprehensiveTrend(ChargeComprehensiveParam param);

    List<ChargeStatisticsVo> getAreaOrRouteComprehensiveDetail(ChargeComprehensiveParam param);

}
