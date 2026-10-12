package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.prepaid.server.po.WaterRechargePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水表充值记录
 */
@Mapper
public interface WaterRechargeMapper extends BaseMapper<WaterRechargePO> {
}
