package com.guhecloud.subsystem.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.context.SubsystemContextHolder;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.listener.web.model.SubsystemRequester;
import com.guhecloud.subsystem.config.util.SubsystemUtils;
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

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubsystemRedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public void removeConfig(String key) {
        this.redisTemplate.delete(key);
    }

    public void refreshConfig(String key, Map map) {
        this.redisTemplate.opsForHash().putAll(key, map);
    }

    public <T> T getRefreshConfig(String key, Class<T> tClass) {
        Object value = this.redisTemplate.opsForValue().get(key);
        String objectValue = getStringObjectMap(value);
        if (StringUtils.isBlank(objectValue)) {
            return null;
        }
        return JsonUtils.deserialize(objectValue, tClass);
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

    public Map<Object, Object> getRefreshConfigs(String key) {
        Set<String> keys = this.redisTemplate.keys("DICTIONARY:" + key + "*");
        Map<Object, Object> result = new HashMap<>();
        for (String string : keys) {
            Object object = this.redisTemplate.opsForValue().get(string);
            result.put(string, object);
        }
        return result;
    }

    public List<SubsystemEndpointConfig> getSubEndpointRefreshConfigs(String subsystemCode) {
        Map<Object, Object> map = this.getRefreshConfigs(SubsystemUtils.SUBSYSTEM_ENDPOINT_CONFIG_KEY);
        List<SubsystemEndpointConfig> subsystemEndpointConfigs = new ArrayList<>();
        map.keySet().stream().forEach(key -> {
            String stringObject = getStringObjectMap(map.get(key));
            if (key.toString().contains(subsystemCode) && StringUtils.isNotBlank(stringObject)) {
                subsystemEndpointConfigs.add(JsonUtils.deserialize(stringObject, SubsystemEndpointConfig.class));
            }
        });
        return subsystemEndpointConfigs;
    }

    public SubsystemEndpointConfig getSubEndpointRefreshConfig(String hashKey) {
        if (StringUtils.isBlank(hashKey)) {
            return null;
        }
        //SubsystemType:tenant
        String[] hashSplit = hashKey.split(":");
        String redisKey = this.getRedisKey(SubsystemUtils.SUBSYSTEM_ENDPOINT_CONFIG_KEY, hashSplit[1], hashSplit[0]);
        return this.getRefreshConfig(redisKey, SubsystemEndpointConfig.class);
    }

    public SubsystemAdapterConfig getSubAdapterRefreshConfig(String hashKey) {
        if (StringUtils.isBlank(hashKey)) {
            return null;
        }
        //SubsystemType:Manufacturer:tenant
        String[] hashSplit = hashKey.split(":");
        String redisKey = this.getRedisKey(SubsystemUtils.SUBSYSTEM_ADAPTER_CONFIG_KEY, hashSplit[2], hashSplit[0] + ":" + hashSplit[1]);
        return this.getRefreshConfig(redisKey, SubsystemAdapterConfig.class);
    }

    public SubsystemAdapterConfig getSubAdapterRefreshConfig() {
        SubsystemRequester subsystemRequester = SubsystemContextHolder.getRequester();
        String redisKey = this.getRedisKey(SubsystemUtils.SUBSYSTEM_ADAPTER_CONFIG_KEY, SessionContextHolder.getRequester().getTenant()
                .toString(), subsystemRequester.getSubsystemCode() + ":" + subsystemRequester.getManufacturer());
        return this.getRefreshConfig(redisKey, SubsystemAdapterConfig.class);
    }

    public void pushMessage(String key, Object message) {
        this.redisTemplate.convertAndSend(key, message);
    }

    private String getRedisKey(String itemCode, String tenant, String code) {
        return "DICTIONARY:" + itemCode + ":" + tenant + ":" + code;
    }
}
