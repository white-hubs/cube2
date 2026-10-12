package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetStandardRelationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划对象的执行标准关联关系
 */
@Mapper
public interface DailyPlanTargetStandardRelationMapper extends BaseMapper<DailyPlanTargetStandardRelationPO> {
}
