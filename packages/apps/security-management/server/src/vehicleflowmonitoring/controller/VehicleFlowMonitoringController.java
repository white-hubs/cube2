package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.param.VehicleFlowMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.service.VehicleFlowMonitoringService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringTimeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
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
public class VehicleFlowMonitoringController {

    private final VehicleFlowMonitoringService vehicleFlowMonitoringService;

    /**
     * @Description 车流分页
     * @Author lrj
     */
    @GetMapping("/vehicle-flow-monitoring-page")
    public Pagination<VehicleFlowMonitoringVo> getVehicleFlowMonitoringPage(VehicleFlowMonitoringParam param) {
        return vehicleFlowMonitoringService.getVehicleFlowMonitoringPage(param);
    }

    /**
     * @Description 隧道今日车流量分析
     * @Author lrj
     */
    @GetMapping("/analysis-today-traffic-flow-tunnels")
    public Map<String, Object> getAnalysisTodayTrafficFlowTunnels(VehicleFlowMonitoringParam param) {
        return vehicleFlowMonitoringService.getAnalysisTodayTrafficFlowTunnels(param);
    }

    /**
     * @Description 隧道不同车道车流量分析
     * @Author lrj
     */
    @GetMapping("/analysis-different-lanes-tunnels")
    public List<VehicleFlowMonitoringTimeVo> getAnalysisDifferentLanesTunnels(VehicleFlowMonitoringParam param) {
        return vehicleFlowMonitoringService.getAnalysisDifferentLanesTunnels(param);
    }

    /**
     * @Description 断面实时平均车速分析
     * @Author lrj
     */
    @GetMapping("/real-average-speed-analysis")
    public Map<String, Object> getRealAverageSpeedAnalysis(VehicleFlowMonitoringParam param) {
        return vehicleFlowMonitoringService.getRealAverageSpeedAnalysis(param);
    }

}
