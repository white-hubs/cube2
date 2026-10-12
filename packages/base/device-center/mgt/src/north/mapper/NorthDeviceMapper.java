package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.device.DevicePropCacheBo;
import com.guhecloud.cube.iot.mgt.north.model.DevDevice;
import com.guhecloud.cube.iot.mgt.north.param.DeviceDetailParam;
import com.guhecloud.cube.iot.mgt.north.param.DeviceQueryParam;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceDetail;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceExtConfig;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceThin;
import com.guhecloud.cube.iot.mgt.north.vo.PropPair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NorthDeviceMapper extends BaseMapper<DevDevice> {

    Page<DeviceThin> pageDevice(Page<Object> convert, @Param("deviceQueryParam") DeviceQueryParam deviceQueryParam,
                                @Param("productId") Long productId
                               );

    DeviceDetail queryDetail(@Param("deviceDetailParam") DeviceDetailParam deviceDetailParam);

    List<PropPair> getDevicePropName(@Param("deviceId") Long deviceId);

    Long queryProductId(@Param("deviceId") Long deviceId);

    DevicePropCacheBo queryDevicePropByIdAndCode(@Param("deviceId") Long deviceId, @Param("propCode") String propCode);

    DeviceDigestVo queryDeviceCache(@Param("deviceId") Long deviceId);

    DeviceExtConfig queryDeviceExtConfig(@Param("deviceId") Long deviceId);

    List<String> listDeviceIdByRawDeviceId(@Param("rawDeviceId") String rawDeviceId);
}