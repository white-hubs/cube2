package com.guhecloud.cube.base.alarm.server.riskscore.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.po.SafetyRiskScorePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 15:50
 */
@Mapper
public interface MarketSafetyRiskScoreMapper extends BaseMapper<SafetyRiskScorePO> {
}
