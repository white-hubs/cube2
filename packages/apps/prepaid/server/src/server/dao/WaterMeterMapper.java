package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.prepaid.server.po.WaterMeterPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水表mapper
 */
@Mapper
public interface WaterMeterMapper extends BaseMapper<WaterMeterPO> {
}
