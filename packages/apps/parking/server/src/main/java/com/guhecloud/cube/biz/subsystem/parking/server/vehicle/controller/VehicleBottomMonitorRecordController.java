package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordStatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleBottomMonitorRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleBottomMonitorRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTypeTotalVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车底监测接口管理
 *
 * @author lanchun
 * @date 2024/12/12 17:47
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleBottomMonitorRecordController {

    private final VehicleBottomMonitorRecordService vehicleBottomMonitorRecordService;

    /**
     * 车底监测分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/vehicle/bottom-monitor/record/page")
    public Pagination<VehicleBottomMonitorRecordPageVO> getPage(VehicleBottomMonitorRecordQueryParam param) {
        return this.vehicleBottomMonitorRecordService.getPage(param);
    }

    /**
     * 来访趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/vehicle/bottom-monitor/record/statistics/reason-tread")
    public List<ReasonTrendTotalVO> getReasonThreadTotal(VehicleBottomMonitorRecordStatisticsQuery param) {
        return this.vehicleBottomMonitorRecordService.getReasonThreadTotal(param);
    }

    /**
     * 来访事由分类统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/vehicle/bottom-monitor/record/statistics/reason-type")
    public List<ReasonTypeTotalVO> getReasonType(VehicleBottomMonitorRecordStatisticsQuery param) {
        return this.vehicleBottomMonitorRecordService.getReasonType(param);
    }
}
