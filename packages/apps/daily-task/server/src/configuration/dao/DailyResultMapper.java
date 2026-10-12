package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行结果
 */
@Mapper
public interface DailyResultMapper extends BaseMapper<DailyResultPO> {
}
