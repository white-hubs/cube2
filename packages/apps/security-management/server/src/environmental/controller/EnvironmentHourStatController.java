package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.EnvironmentHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.HourStatAvgResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PieGraphResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PmTwoPointFiveStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.TrendResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.EnvironmentHourStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 环境监测小时统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnvironmentHourStatController {
    private final EnvironmentHourStatService environmentHourStatService;

    /**
     * PM2.5实时统计
     *
     * @return 统计
     */
    @GetMapping("/environmental/stat/ue-pm25")
    public PmTwoPointFiveStatResult realtimePmTwoPointFiveStat() {
        return environmentHourStatService.realtimePmTwoPointFiveStat();
    }

    /**
     * 平均值统计
     *
     * @param param 条件入参
     *
     * @return 平均值统计
     */
    @GetMapping("/environmental/stat/hourStatAvg")
    public HourStatAvgResult hourStatAvg(EnvironmentHourParam param) {
        return environmentHourStatService.hourStatAvg(param);
    }

    /**
     * 环境监测趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/environmental/stat/trend")
    public List<TrendResult> getAirQualityTrend(EnvironmentHourParam param) {
        return environmentHourStatService.getAirQualityTrend(param);
    }

    /**
     * 质量统计饼图
     *
     * @param param 条件入参
     *
     * @return 统计
     */
    @GetMapping("/environmental/stat/pie-graph")
    public List<PieGraphResult> getPieGraph(EnvironmentHourParam param) {
        return environmentHourStatService.getPieGraph(param);
    }

    /**
     * pm2.5小时统计环比(日环比+周环比)
     * 游仙区小微产业驾驶舱定制接口
     *
     * @return 环比
     */
    @GetMapping("/environmental/stat/day-andWeek-ratio")
    public QoqStatResult dayAndWeekRatio() {
        return environmentHourStatService.dayAndWeekRatio();
    }

}
