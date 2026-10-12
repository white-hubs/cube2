package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * cron表达式
 */
@Mapper
public interface DailyCronMapper extends BaseMapper<DailyCronPO> {
}
