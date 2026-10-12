package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行项
 */
@Mapper
public interface DailyItemMapper extends BaseMapper<DailyItemPO> {
}
