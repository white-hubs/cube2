package com.guhecloud.cube.base.device.center.core.rule.tag.cache;

import com.guhecloud.cube.base.device.center.core.rule.tag.cache.entity.TagRule;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class TagRuleRedisCache {

    private static final String DEVICE_TAG_RULE = "DEVICE_TAG_RULE";

    private final RedisTemplate<String, Object> redisTemplate;

    public TagRuleRedisCache(RedisTemplate<String, Object> redisTemplate) {this.redisTemplate = redisTemplate;}

    public void saveCache(TagRule tagRule) {
        this.redisTemplate.opsForHash()
                .put(DEVICE_TAG_RULE, getHashKey(tagRule.getCode()), tagRule);
    }

    public Optional<TagRule> getCache(String tagCode) {
        Object o = this.redisTemplate.opsForHash().get(DEVICE_TAG_RULE, getHashKey(tagCode));
        if (Objects.nonNull(o)) {
            return Optional.of((TagRule) o);
        }
        return Optional.empty();
    }

    public Map<String, TagRule> getAllCache() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(DEVICE_TAG_RULE);
        if (MapUtils.isEmpty(entries)) {
            return Collections.emptyMap();
        }
        Map<String, TagRule> tagRuleMap = new HashMap<>();
        entries.forEach((k, v) ->
                                tagRuleMap.put(k.toString(), (TagRule) v)
                       );
        return tagRuleMap;
    }

    private String getHashKey(String code) {
        return SessionContextHolder.getRequester().getTenant() + "_" + code;
    }
}
