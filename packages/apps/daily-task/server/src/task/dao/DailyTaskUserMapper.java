package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务人员
 */
@Mapper
public interface DailyTaskUserMapper extends BaseMapper<DailyTaskUserPO> {
}
