package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerMeter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransformerMeterMapper extends BaseMapper<TransformerMeter> {
    TransformerMeter getLastOne(@Param("deviceId") Long deviceId);
}
