package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划的对象
 */
@Mapper
public interface DailyPlanTargetRelationMapper extends BaseMapper<DailyPlanTargetRelationPO> {
}
