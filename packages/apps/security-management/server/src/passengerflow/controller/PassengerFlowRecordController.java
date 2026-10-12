package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.param.PassengerFlowStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.service.PassengerFlowRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.RankingPassengerFlowVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassengerFlowRecordController {

    private final PassengerFlowRecordService passengerFlowRecordService;

    /**
     * @Description客流统计（人次）
     * @Author lrj
     */
    @GetMapping("/passenger-flow-statistics")
    public Map<String, Object> getPassengerFlowStatistics(PassengerFlowStatisticsParam param) {
        return passengerFlowRecordService.getPassengerFlowStatistics(param);
    }

    /**
     * 各区域客流量排名top
     *
     * @Author lrj
     */
    @GetMapping("/ranking-passenger-flow-regions-top")
    public List<RankingPassengerFlowVo> getRankingPassengerFlowRegionsTop(PassengerFlowStatisticsParam param) {
        return passengerFlowRecordService.getRankingPassengerFlowRegions("topFlag", param);
    }

    /**
     * 在馆客流分析图
     *
     * @Author lrj
     */
    @GetMapping("/analysis-chart-passenger-flow-museum")
    public Map<String, Object> getAnalysisChartPassengerMuseum(PassengerFlowStatisticsParam param) {
        return passengerFlowRecordService.getAnalysisChartPassengerMuseum(param);
    }

    /**
     * 各区域客流量排名
     *
     * @Author lrj
     */
    @GetMapping("/ranking-passenger-flow-regions")
    public List<RankingPassengerFlowVo> getRankingPassengerFlowRegions(PassengerFlowStatisticsParam param) {
        return passengerFlowRecordService.getRankingPassengerFlowRegions("", param);
    }

}
