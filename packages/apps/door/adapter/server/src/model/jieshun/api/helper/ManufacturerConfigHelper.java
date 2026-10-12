package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.ManufacturerEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * author: ht
 * create: 2025-05-23 11:11
 */
@Component
@RequiredArgsConstructor
public class ManufacturerConfigHelper {
    private final SubsystemRedisUtils subsystemRedisUtils;

    public <T> T getManufacturerConfig(ManufacturerEnum manufacturerEnum, Class<T> tClass) {
        SubsystemAdapterConfig basicConfig = getBasicConfig(manufacturerEnum);
        Object manufacturerJson = basicConfig.getManufacturerJson();
        return JsonUtils.deserialize(manufacturerJson, tClass);

    }

    /**
     * 获取门禁基础配置
     */
    public SubsystemAdapterConfig getBasicConfig(ManufacturerEnum manufacturerEnum) {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + manufacturerEnum.getCode() + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }
}
