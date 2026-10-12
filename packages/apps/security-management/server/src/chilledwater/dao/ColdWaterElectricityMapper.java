package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWaterElectricity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface ColdWaterElectricityMapper extends BaseMapper<ColdWaterElectricity> {
    ColdWaterElectricity getQuantityElectricity(@Param("deviceId") Long deviceId, @Param("date") String date);

    BigDecimal getColdWaterElectricity(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
