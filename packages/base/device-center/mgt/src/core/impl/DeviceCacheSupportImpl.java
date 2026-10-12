package com.guhecloud.cube.iot.mgt.core.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceState;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DeviceCacheSupportImpl implements DeviceCacheSupport {

    //根据设备ID缓存摘要信息
    private final Cache<Long, DeviceDigestVo> deviceCacheVoCache = CacheBuilder.newBuilder()
            .initialCapacity(2048)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Resource
    private NorthDeviceMapper northDeviceMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<DeviceDigestVo> queryDeviceDigestCache(Long deviceId) {
        DeviceDigestVo deviceDigestVo = deviceCacheVoCache.getIfPresent(deviceId);
        if (Objects.isNull(deviceDigestVo)) {
            deviceDigestVo = reloadDigestToCache(deviceId);
        }

        return Optional.ofNullable(deviceDigestVo);
    }

    /**
     * 查询设备摘要信息(存在就进行缓存)
     *
     * @param deviceId 设备ID
     *
     * @return 设备摘要信息
     */
    private DeviceDigestVo reloadDigestToCache(Long deviceId) {
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(deviceId);

        if (Objects.isNull(deviceDigestVo)) {
            deviceCacheVoCache.invalidate(deviceId);
            return null;
        } else {
            deviceCacheVoCache.put(deviceId, deviceDigestVo);
            return deviceDigestVo;
        }
    }

    @Override
    public Optional<DevicePropValueVo> queryDevicePropCache(Long deviceId, String propCode) {
        String redisKey = DEVICE_PROP_KEY.replace("{device-id}", deviceId.toString());
        Object object = redisTemplate.opsForHash().get(redisKey, propCode);

        if (Objects.isNull(object)) {
            return Optional.empty();
        }

        String objStr = object.toString();
        if (StringUtils.isBlank(objStr)) {
            return Optional.empty();
        }

        JSONObject jsonObject = JSON.parseObject(objStr);
        if (Objects.isNull(jsonObject)) {
            return Optional.empty();
        }

        DevicePropValueVo devicePropValueVo = new DevicePropValueVo();
        devicePropValueVo.setTs(jsonObject.getLongValue("ts"));
        devicePropValueVo.setValue(jsonObject.getString("value"));
        devicePropValueVo.setPreValue(jsonObject.getString("preValue"));
        return Optional.of(devicePropValueVo);
    }

    @Override
    public Optional<DeviceState> queryDeviceStateCache(Long deviceId) {
        String redisKey = DEVICE_STATE_KEY.replace("{device-id}", String.valueOf(deviceId));
        Object object = redisTemplate.opsForValue().get(redisKey);

        if (Objects.isNull(object)) {
            //如果没有缓存，则写入一个空的设备状态
            return Optional.empty();
        }

        return Optional.ofNullable(JSON.parseObject(object.toString(), DeviceState.class));
    }
}
