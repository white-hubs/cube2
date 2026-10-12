package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoVisualPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingVisualMapper extends BaseMapper<ParkingInfoVisualPO> {
}
