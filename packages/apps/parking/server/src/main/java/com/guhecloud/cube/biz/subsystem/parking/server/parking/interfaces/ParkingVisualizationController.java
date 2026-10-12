package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces;

import com.guhecloud.cube.biz.subsystem.parking.client.param.StatisticalChartParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingVisualizationService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkStatisticsVo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyTypeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆管理可视化
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingVisualizationController {
    private final ParkingVisualizationService visualizationService;

    /**
     * 车位总览
     */
    @GetMapping("/parking/getParkingOverview")
    public ParkingOverviewVO getParkingOverview(@RequestParam(required = false) String source) {
        return visualizationService.getParkingOverview(source);
    }

    /**
     * 收费总览
     */
    @GetMapping("/parking/getMoneyOverview")
    public MoneyOverviewVO getMoneyOverview(StatisticalChartParam param) {
        return visualizationService.getMoneyOverview(param);
    }

    /**
     * 车流趋势
     */
    @GetMapping("/parking/getTrafficTrend")
    public StatisticalChartVO getTrafficTrend(StatisticalChartParam param) {
        return visualizationService.getTrafficTrend(param);
    }

    /**
     * 滞留车流分布
     */
    @GetMapping("/parking/getRetentionTrafficTrend")
    public StatisticalChartVO getRetentionTrafficTrend() {
        return visualizationService.getRetentionTrafficTrend();
    }

    /**
     * 停车时长分布
     */
    @GetMapping("/parking/getParkingDuration")
    public StatisticalChartVO getParkingDuration(StatisticalChartParam param) {
        return visualizationService.getParkingDuration(param);
    }

    /**
     * 支付方式比例
     */
    @GetMapping("/parking/getPayType")
    public List<MoneyTypeVO> getPayType(StatisticalChartParam param) {
        return visualizationService.getPayType(param);
    }

    /**
     * 收费趋势
     */
    @GetMapping("/parking/getPayTrend")
    public StatisticalChartVO getPayTrend(StatisticalChartParam param) {
        return visualizationService.getPayTrend(param);
    }

    /**
     * 收费区间分布
     */
    @GetMapping("/parking/getPayIntervalTrend")
    public StatisticalChartVO getPayIntervalTrend(StatisticalChartParam param) {
        return visualizationService.getPayIntervalTrend(param);
    }

    /**
     * @Description 停车进出统计
     * @Author lrj
     */
    @GetMapping("/park-in-out")
    public ParkStatisticsVo getParkEventInOrOut(ParkingStatisticsQueryParam param) {
        return visualizationService.getParkEventInOrOut(param);
    }
}
