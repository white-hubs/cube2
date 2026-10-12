package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskItemPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务的执行内容
 */
@Mapper
public interface DailyTaskItemMapper extends BaseMapper<DailyTaskItemPO> {
}
