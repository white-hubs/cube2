package com.guhecloud.cube.iot.mgt.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.device.model.DevDataShare;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceIdCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DevDataShareMapper extends BaseMapper<DevDataShare> {

    List<DeviceIdCountVo> countAsSourceByDeviceIds(@Param("deviceIds") Collection<Long> deviceIds,
                                                   @Param("tenant") Integer tenant);

    List<DeviceIdCountVo> countAsTargetByDeviceIds(@Param("deviceIds") Collection<Long> deviceIds,
                                                   @Param("tenant") Integer tenant);
}
