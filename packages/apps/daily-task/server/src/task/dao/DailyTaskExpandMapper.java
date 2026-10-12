package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskExpandPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务扩展信息
 */
@Mapper
public interface DailyTaskExpandMapper extends BaseMapper<DailyTaskExpandPO> {
}
