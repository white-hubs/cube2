package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 15:41
 * @Description:
 */
@Mapper
public interface IllegalParkingRecordMapper extends BaseMapper<IllegalParkingRecordPO> {
}
