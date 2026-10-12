package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyUrgencyPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 紧急程度
 */
@Mapper
public interface DailyUrgencyMapper extends BaseMapper<DailyUrgencyPO> {
}
