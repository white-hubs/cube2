package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.EnvironmentHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.ContrastResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.HourStatAvgResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PieGraphResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PmTwoPointFiveStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.TrendResult;
import com.guhecloud.platform.base.model.Period;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 环境监测小时统计服务
 */
public interface EnvironmentHourStatService {
    void gatherInto(LocalDateTime nowTime);

    PmTwoPointFiveStatResult realtimePmTwoPointFiveStat();

    HourStatAvgResult hourStatAvg(EnvironmentHourParam param);

    List<TrendResult> getAirQualityTrend(EnvironmentHourParam param);

    List<PieGraphResult> getPieGraph(EnvironmentHourParam param);

    /**
     * 统计比率
     *
     * @param period 时间段
     * @param type   比率类型
     *
     * @return 比率
     */
    ContrastResult getHourStatRatio(Period period, ChronoUnit type);

    /**
     * pm2.5小时统计环比(日环比+周环比)
     * 游仙区小微产业驾驶舱定制接口
     *
     * @return 环比
     */
    QoqStatResult dayAndWeekRatio();
}
