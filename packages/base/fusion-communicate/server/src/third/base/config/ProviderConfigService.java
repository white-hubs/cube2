package com.guhecloud.cube.biz.fusion.communicate.server.third.base.config;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.event.ProviderConfigChangeEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProviderConfigService implements IProviderConfig {

    private final DataDictionaryClient dataDictionaryClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    public ProviderConfigService(DataDictionaryClient dataDictionaryClient,
                                 RedisTemplate<String, Object> redisTemplate,
                                 ApplicationEventPublisher applicationEventPublisher
                                ) {
        this.dataDictionaryClient = dataDictionaryClient;
        this.redisTemplate = redisTemplate;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public ProviderConfig getProviderConfig(String key) {
        Object o = this.redisTemplate.opsForValue().get(getConfigCacheKey(key));
        if (Objects.nonNull(o)) {
            return (ProviderConfig) o;
        } else {
            return getDataDictionaryConfig(key);
        }
    }

    public ProviderConfig getDataDictionaryConfig(String key) {
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(SUB_SYSTEM_CODE);
        dataDictionaryaramParam.setCode(key);
        List<DataDictionaryDTO> dataDictionaryByItem = this.dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(dataDictionaryByItem)) {
            return null;
        }
        if (StringUtils.isBlank(dataDictionaryByItem.get(0).getVariable())) {
            return null;
        }
        ProviderConfig providerConfig = JsonUtils
                .deserialize(dataDictionaryByItem.get(0).getVariable(), ProviderConfig.class);
        providerConfig.setTenant(SessionContextHolder.getRequester().getTenant());
        return providerConfig;
    }

    @Override
    public void refreshProviderConfig(String key) {
        String configCacheKey = getConfigCacheKey(key);
        Object o = this.redisTemplate.opsForValue().get(configCacheKey);
        ProviderConfig newConfig = getDataDictionaryConfig(key);
        if (Objects.isNull(newConfig)) {
            this.redisTemplate.delete(configCacheKey);
            return;
        }
        this.redisTemplate.opsForValue().set(configCacheKey, newConfig);
        if (Objects.isNull(o)) {
            //发送事件
            this.applicationEventPublisher.publishEvent(new ProviderConfigChangeEvent(this, null, newConfig));
            return;
        }
        ProviderConfig oldConfig = (ProviderConfig) o;
        if (!oldConfig.equals(newConfig)) {
            //发送事件
            this.applicationEventPublisher.publishEvent(new ProviderConfigChangeEvent(this, (ProviderConfig) o, newConfig));
        }
    }

    @Override
    public List<ProviderConfig> getAllTenantProviderConfig(String key) {
        Set<String> keys = this.redisTemplate.keys(key.replace("::", ":") + "*");
        return keys.stream().map(k -> {
            Object o = this.redisTemplate.opsForValue().get(k);
            return (ProviderConfig) o;
        }).collect(Collectors.toList());
    }

    public String getConfigCacheKey(String key) {
        return String.format("%s:%s", key.replace("::", ":"), SessionContextHolder.getRequester().getTenant());
    }
}
