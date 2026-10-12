package com.guhecloud.cube.srv.gen.park.management.server.interfaces;

import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.ParkFloorsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.UtilizationRateVo;
import com.guhecloud.cube.srv.gen.park.management.server.service.ParkDataStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description 三维数据统计
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkDataStatisticsController {

    private final ParkDataStatisticsService parkDataStatisticsService;

    /**
     * @Description 空间利用率
     * @Author lrj
     */
    @GetMapping("/space-utilization-rate")
    public Map<String, Object> getSpaceUtilizationRate() {
        return parkDataStatisticsService.getSpaceUtilizationRate();
    }

    /**
     * @Description 楼栋空间利用率
     * @Author lrj
     */
    @GetMapping("/building-space-utilization-rate")
    public List<UtilizationRateVo> getBuildingSpaceUtilizationRate() {
        return parkDataStatisticsService.getBuildingSpaceUtilizationRate();
    }

    /**
     * @Description 租赁企业行业统计
     * @Author lrj
     */
    @GetMapping("/industry-statistics-enterprises")
    public Map<String, Object> getIndustryStatisticsEnterprises() {
        return parkDataStatisticsService.getIndustryStatisticsEnterprises();
    }

    /**
     * @Description 园区租赁总情况
     * @Author lrj
     */
    @GetMapping("/overall-situation-park-leasing")
    public Map<String, Object> getOverallSituationParkLeasing() {
        return parkDataStatisticsService.getOverallSituationParkLeasing();
    }

    /**
     * @Description 园区租赁趋势
     * @Author lrj
     */
    @GetMapping("/rental-trends")
    public Map<String, Object> getRentalTrends() {
        return parkDataStatisticsService.getRentalTrends();
    }

    /**
     * @Description 园区楼层租赁比情况
     * @Author lrj
     */
    @GetMapping("/rental-ratio-park-floors/{areaId}")
    public List<ParkFloorsVo> getRentalRatioParkFloors(@PathVariable String areaId) {
        return parkDataStatisticsService.getRentalRatioParkFloors(areaId);
    }

}
