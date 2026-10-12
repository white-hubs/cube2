package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardTargetTypeRelationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行标准与目标类型关联关系
 */
@Mapper
public interface DailyStandardTargetTypeRelationMapper extends BaseMapper<DailyStandardTargetTypeRelationPO> {
}

