package com.guhecloud.cube.biz.daily.task.configuration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyStandardItemRelationMapper extends BaseMapper<DailyStandardItemResultRelationPO> {
}
