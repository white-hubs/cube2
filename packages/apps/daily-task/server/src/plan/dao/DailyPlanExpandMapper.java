package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanExpandPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划附加信息
 */
@Mapper
public interface DailyPlanExpandMapper extends BaseMapper<DailyPlanExpandPO> {
}
