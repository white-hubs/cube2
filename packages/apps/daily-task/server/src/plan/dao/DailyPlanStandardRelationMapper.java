package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanStandardRelationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划的执行项
 */
@Mapper
public interface DailyPlanStandardRelationMapper extends BaseMapper<DailyPlanStandardRelationPO> {
}
