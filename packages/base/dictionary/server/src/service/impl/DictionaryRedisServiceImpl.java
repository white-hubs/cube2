package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.guhecloud.cube.base.dictionary.server.service.DictionaryRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  11:09
 * @Description:
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryRedisServiceImpl implements DictionaryRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);

    }

    @Override
    public Boolean delete(String key) {
        return this.redisTemplate.delete(key);
    }
}
