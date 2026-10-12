package com.guhecloud.cube.customization.xialing.server.drone.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警派飞配置数据访问接口。
 */
@Mapper
public interface DroneAlarmDispatchConfigMapper extends BaseMapper<DroneAlarmDispatchConfig> {
}
