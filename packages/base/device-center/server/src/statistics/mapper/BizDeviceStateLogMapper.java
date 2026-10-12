package com.guhecloud.cube.base.device.center.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 设备状态变更记录
 * @date 2024/1/22 16:52
 */
@Mapper
public interface BizDeviceStateLogMapper extends BaseMapper<BizDeviceStateLog> {

    Integer getLastState(@Param("deviceId") Long deviceId);

    List<BizDeviceStateLog> selectLogByDeviceId(@Param("deviceId") Long id);

    Integer countOffLineNum(@Param("deviceId") Long id);
}
