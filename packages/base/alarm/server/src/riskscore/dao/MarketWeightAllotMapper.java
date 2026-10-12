package com.guhecloud.cube.base.alarm.server.riskscore.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.po.WeightAllotPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wql
 * @describe 权重分配
 * @date 2023/10/12 15:51
 */
@Mapper
public interface MarketWeightAllotMapper extends BaseMapper<WeightAllotPO> {
}
