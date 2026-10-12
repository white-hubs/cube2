package com.guhecloud.cube.base.dictionary.server.service;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  11:05
 * @Description:
 * @Version: 1.0
 */
public interface DictionaryRedisService {
    void set(String key, String value);

    Boolean delete(String key);
}
