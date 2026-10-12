package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleBottomMonitorRecord;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordStatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTypeTotalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehicleBottomMonitorRecordMapper extends BaseMapper<VehicleBottomMonitorRecord> {

    /**
     * 来访趋势统计
     *
     * @param param
     *
     * @return
     */
    List<ReasonTrendTotalVO> selectReasonTrendStatistics(@Param("param") VehicleBottomMonitorRecordStatisticsQuery param);

    /**
     * 来访事由分组统计
     *
     * @param param
     *
     * @return
     */
    List<ReasonTypeTotalVO> selectReasonTypeStatistics(@Param("param") VehicleBottomMonitorRecordStatisticsQuery param);
}
