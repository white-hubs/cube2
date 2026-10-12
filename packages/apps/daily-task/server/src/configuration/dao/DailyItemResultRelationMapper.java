package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行项的结果关联表
 */
@Mapper
public interface DailyItemResultRelationMapper extends BaseMapper<DailyItemResultRelationPO> {
}
