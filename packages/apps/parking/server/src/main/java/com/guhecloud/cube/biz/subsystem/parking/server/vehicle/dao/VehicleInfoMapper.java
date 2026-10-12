package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆信息
 */
@Mapper
public interface VehicleInfoMapper extends BaseMapper<VehicleInfo> {
}
