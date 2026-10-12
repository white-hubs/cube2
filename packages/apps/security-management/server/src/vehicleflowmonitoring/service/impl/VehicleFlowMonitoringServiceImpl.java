package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.dao.VehicleFlowMonitoringMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.entity.VehicleFlowMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.enumeration.DirectionEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.param.VehicleFlowMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.service.VehicleFlowMonitoringService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringTimeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleFlowMonitoringServiceImpl extends ServiceImpl<VehicleFlowMonitoringMapper, VehicleFlowMonitoring> implements VehicleFlowMonitoringService {

    private final VehicleFlowMonitoringMapper vehicleFlowMonitoringMapper;

    @Override
    public Pagination<VehicleFlowMonitoringVo> getVehicleFlowMonitoringPage(VehicleFlowMonitoringParam param) {
        LocalDateTime st = null;
        LocalDateTime et = null;
        if (Objects.nonNull(param.getPeriod())) {
            st = param.getPeriod().getBegin();
            et = param.getPeriod().getEnd();
        }
        return PagingUtils.convert(vehicleFlowMonitoringMapper.getVehicleFlowMonitoringPage(PagingUtils.convert(param), st, et));
    }

    @Override
    public Map<String, Object> getAnalysisTodayTrafficFlowTunnels(VehicleFlowMonitoringParam param) {
        Map<String, Object> remap = new HashMap<>();
        LocalDateTime st = null;
        LocalDateTime et = null;
        if (Objects.nonNull(param.getPeriod())) {
            st = param.getPeriod().getBegin();
            et = param.getPeriod().getEnd();
        }
        //驶入
        int directionTotalIn = vehicleFlowMonitoringMapper.getDirectionTotal(DirectionEnum.DIRECTION_IN.name(), st, et);
        //驶出
        int directionTotalOut = vehicleFlowMonitoringMapper.getDirectionTotal(DirectionEnum.DIRECTION_OUT.name(), st, et);
        Map<String, Object> today = SecurityManagementCommonUtil.getDateFormat("TODAY", st, et);
        String timeFlag = (String) today.get("timeFlag");
        List<String> xtitle = (List<String>) today.get("xtitle");
        //驶入列表
        List<VehicleFlowMonitoringTimeVo> directionInList = vehicleFlowMonitoringMapper.getDirectionList(timeFlag, DirectionEnum.DIRECTION_IN.name(), st, et);
        //驶出列表
        List<VehicleFlowMonitoringTimeVo> directionOutList = vehicleFlowMonitoringMapper.getDirectionList(timeFlag, DirectionEnum.DIRECTION_OUT.name(), st, et);
        remap.put("xtitle", xtitle);
        remap.put("directionInList", directionInList);
        remap.put("directionOutList", directionOutList);
        remap.put("directionTotalIn", directionTotalIn);
        remap.put("directionTotalOut", directionTotalOut);
        return remap;
    }

    @Override
    public List<VehicleFlowMonitoringTimeVo> getAnalysisDifferentLanesTunnels(VehicleFlowMonitoringParam param) {
        Map<String, Object> today = SecurityManagementCommonUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                .getPeriod().getEnd());
        LocalDateTime st = (LocalDateTime) today.get("st");
        LocalDateTime et = (LocalDateTime) today.get("et");
        List<VehicleFlowMonitoringTimeVo> analysisDifferentLanesTunnels = vehicleFlowMonitoringMapper.getAnalysisDifferentLanesTunnels(st, et);
        return analysisDifferentLanesTunnels;
    }

    @Override
    public Map<String, Object> getRealAverageSpeedAnalysis(VehicleFlowMonitoringParam param) {
        LocalDateTime st = null;
        LocalDateTime et = null;
        if (Objects.nonNull(param.getPeriod())) {
            st = param.getPeriod().getBegin();
            et = param.getPeriod().getEnd();
        }
        Map<String, Object> today = SecurityManagementCommonUtil.getDateFormat("TODAY", st, et);
        String timeFlag = (String) today.get("timeFlag");
        List<String> xtitle = (List<String>) today.get("xtitle");
        //驶入
        List<VehicleFlowMonitoringTimeVo> realAverageSpeedAnalysisIn = vehicleFlowMonitoringMapper.getRealAverageSpeedAnalysis(timeFlag, DirectionEnum.DIRECTION_IN.name(), st, et);
        //驶出
        List<VehicleFlowMonitoringTimeVo> realAverageSpeedAnalysisOut = vehicleFlowMonitoringMapper.getRealAverageSpeedAnalysis(timeFlag, DirectionEnum.DIRECTION_OUT.name(), st, et);
        Map<String, Object> remap = new HashMap<>();
        remap.put("xtitle", xtitle);
        remap.put("realAverageSpeedAnalysisIn", realAverageSpeedAnalysisIn);
        remap.put("realAverageSpeedAnalysisOut", realAverageSpeedAnalysisOut);
        return remap;

    }
}
