package com.guhecloud.cube.iot.mgt.core.def.cache;

import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceState;

import java.util.Optional;

/**
 * 设备缓存支持(设备摘要:支持二级缓存)<br/>
 * <p>
 * 设备摘要(obj): iot:dev:{device-id}:<br/>
 * 设备属性值(hash-map): iot:dev:{device-id}:prop<br/>
 */
public interface DeviceCacheSupport {

    String DEVICE_PROP_KEY = "iot:dev:prop:{device-id}";

    String DEVICE_STATE_KEY = "iot:dev:state:{device-id}";

    /**
     * 查询设备摘要缓存
     *
     * @param deviceId 设备id
     *
     * @return 设备摘要
     */
    Optional<DeviceDigestVo> queryDeviceDigestCache(Long deviceId);

    /**
     * 查询设备状态
     *
     * @param deviceId 设备ID
     *
     * @return 设备状态
     */
    Optional<DeviceState> queryDeviceStateCache(Long deviceId);

    /**
     * 查询设备属性查询
     *
     * @param deviceId 设备id
     * @param propCode 属性code
     *
     * @return 设备属性缓存
     */
    Optional<DevicePropValueVo> queryDevicePropCache(Long deviceId, String propCode);
}
