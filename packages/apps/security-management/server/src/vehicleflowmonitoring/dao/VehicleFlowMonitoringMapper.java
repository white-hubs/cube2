package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.entity.VehicleFlowMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringTimeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo.VehicleFlowMonitoringVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VehicleFlowMonitoringMapper extends BaseMapper<VehicleFlowMonitoring> {
    Page<VehicleFlowMonitoringVo> getVehicleFlowMonitoringPage(Page page, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    int getDirectionTotal(@Param("direction") String Direction, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<VehicleFlowMonitoringTimeVo> getDirectionList(@Param("dateFormat") String dateFormat, @Param("direction") String Direction,
                                                       @Param("st") LocalDateTime st, @Param("et") LocalDateTime et
                                                      );

    List<VehicleFlowMonitoringTimeVo> getAnalysisDifferentLanesTunnels(@Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<VehicleFlowMonitoringTimeVo> getRealAverageSpeedAnalysis(@Param("dateFormat") String dateFormat,
                                                                  @Param("direction") String Direction, @Param("st") LocalDateTime st,
                                                                  @Param("et") LocalDateTime et
                                                                 );

}
