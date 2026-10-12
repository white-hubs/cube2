package com.guhecloud.cube.biz.daily.task.record.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 执行记录
 */
@Mapper
public interface DailyRecordMapper extends BaseMapper<DailyRecordPO> {

    Integer countTargetAbnormalNum(@Param("targetId") Long targetId, @Param("dailyType") String dailyType, @Param("period") Period period);
}
