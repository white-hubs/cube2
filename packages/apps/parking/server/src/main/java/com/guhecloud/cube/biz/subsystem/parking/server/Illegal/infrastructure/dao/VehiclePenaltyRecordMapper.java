package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.VehiclePenaltyRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 车牌扣分记录Mapper
 */
@Mapper
public interface VehiclePenaltyRecordMapper extends BaseMapper<VehiclePenaltyRecordPO> {
}