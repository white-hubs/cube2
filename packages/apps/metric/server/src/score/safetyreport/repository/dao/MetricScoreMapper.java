package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.po.RuleScorePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MetricScoreMapper extends BaseMapper<RuleScorePO> {
}
