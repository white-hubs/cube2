package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.entity.VehicleFlowMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.param.VehicleFlowMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringTimeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface VehicleFlowMonitoringService extends IService<VehicleFlowMonitoring> {
    Pagination<VehicleFlowMonitoringVo> getVehicleFlowMonitoringPage(VehicleFlowMonitoringParam param);

    Map<String, Object> getAnalysisTodayTrafficFlowTunnels(VehicleFlowMonitoringParam param);

    List<VehicleFlowMonitoringTimeVo> getAnalysisDifferentLanesTunnels(VehicleFlowMonitoringParam param);

    Map<String, Object> getRealAverageSpeedAnalysis(VehicleFlowMonitoringParam param);
}
