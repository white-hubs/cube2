package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务类型
 */
@Mapper
public interface DailyTaskCategoryMapper extends BaseMapper<DailyTaskCategoryPO> {
}
