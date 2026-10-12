package com.guhecloud.cube.biz.adapter.park.server.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.mqtt.HikSpccParkMqttClientProtocol;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkMqttClientFactoryManager {
    private final Map<String, ParkMqttClientFactory> factoryMap = new HashMap<>();

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void MqttClientFactoryManager(HikSpccParkMqttClientProtocol hikSpccParkMqttClientProtocol) {
        factoryMap.put("hikspcc", hikSpccParkMqttClientProtocol);
    }

    public void connect() {
        // 获取所有子系统的配置
        List<SubsystemAdapterConfig> configs = getBasicConfig();

        for (SubsystemAdapterConfig config : configs) {
            String manufacturer = config.getManufacturer();
            ParkMqttClientFactory factory = factoryMap.get(manufacturer);
            if (factory != null) {
                SessionContextHolder.requestTenant(config.getTenant());
                factory.connect(config);
            } else {
                log.error("未知厂商：{}", manufacturer);
            }
        }
    }

    /**
     * 实现获取所有子系统配置的逻辑
     *
     * @return
     */
    public List<SubsystemAdapterConfig> getBasicConfig() {
        List<SubsystemAdapterConfig> subsystemAdapterConfigs = new ArrayList<>();
        // 定义匹配模式
        String pattern = "DICTIONARY:SUBSYSTEM_ADAPTER_CONFIG:*";

        // 使用 keys 方法获取所有匹配的键
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) {
            log.warn("No keys found with pattern: {}", pattern);
            return subsystemAdapterConfigs;
        }

        // 过滤出包含 parkingServer 的键
        List<String> filteredKeys = keys.stream()
                .filter(key -> key.contains("parkingServer"))
                .collect(Collectors.toList());

        for (String key : filteredKeys) {
            log.info("Found key: {}", key);
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                String variable = getStringObjectMap(value);
                SubsystemAdapterConfig config = JsonUtils.deserialize(variable, SubsystemAdapterConfig.class);
                if (config != null) {
                    subsystemAdapterConfigs.add(config);
                } else {
                    log.warn("Failed to deserialize value for key: {}", key);
                }
            } else {
                log.warn("No value found for key: {}", key);
            }
        }

        return subsystemAdapterConfigs;
    }

    private static String getStringObjectMap(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JsonUtils.deserialize(value.toString(), new TypeReference<Map<String, Object>>() {
        });
        if (MapUtils.isEmpty(stringObjectMap) || Objects.isNull(stringObjectMap.get("variable"))) {
            return null;
        }
        return stringObjectMap.get("variable").toString();
    }
}
