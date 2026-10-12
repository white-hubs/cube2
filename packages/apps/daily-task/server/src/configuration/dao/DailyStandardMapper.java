package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行标准
 */
@Mapper
public interface DailyStandardMapper extends BaseMapper<DailyStandardPO> {
}
