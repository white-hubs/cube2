package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyJobRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划执行记录
 */
@Mapper
public interface DailyJobRecordMapper extends BaseMapper<DailyJobRecordPO> {

}
