package com.guhecloud.cube.customization.xialing.server.drone.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警派飞日志数据访问接口。
 */
@Mapper
public interface DroneAlarmDispatchRecordMapper extends BaseMapper<DroneAlarmDispatchRecord> {
}
