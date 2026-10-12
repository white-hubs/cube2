package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetItemPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务对象的执行内容
 */
@Mapper
public interface DailyTaskTargetItemMapper extends BaseMapper<DailyTaskTargetItemPO> {
}
