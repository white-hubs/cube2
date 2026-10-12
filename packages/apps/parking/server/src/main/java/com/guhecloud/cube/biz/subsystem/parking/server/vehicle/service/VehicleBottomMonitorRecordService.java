package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleBottomMonitorRecord;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordStatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleBottomMonitorRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTypeTotalVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface VehicleBottomMonitorRecordService extends IService<VehicleBottomMonitorRecord> {

    Pagination<VehicleBottomMonitorRecordPageVO> getPage(VehicleBottomMonitorRecordQueryParam param);

    List<ReasonTrendTotalVO> getReasonThreadTotal(VehicleBottomMonitorRecordStatisticsQuery param);

    List<ReasonTypeTotalVO> getReasonType(VehicleBottomMonitorRecordStatisticsQuery param);
}
