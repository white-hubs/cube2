package com.guhecloud.cube.biz.daily.task.record.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行记录工单
 */
@Mapper
public interface DailyRecordWorkOrderMapper extends BaseMapper<DailyRecordWorkOrderPO> {
}
