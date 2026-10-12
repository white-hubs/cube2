package com.guhecloud.cube.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/8 15:27
 * @Description:
 */

@Component
public class JsonUtil {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 对象序列化
     *
     * @param object 目标对象实例
     *
     * @return JSON字符串
     */
    public String serialize(Object object) {
        try {
            return object == null ? null : objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param json JSON字符串
     * @param type 类型对象
     * @param <T>  对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(String json, Class<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param json JSON字符串
     * @param type 类型引用对象
     * @param <T>  对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param bytes JSON字节数组
     * @param type  类型对象
     * @param <T>   对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(byte[] bytes, Class<T> type) {
        try {
            return bytes == null ? null : objectMapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param bytes JSON字节数组
     * @param type  类型引用对象
     * @param <T>   对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(byte[] bytes, TypeReference<T> type) {
        try {
            return bytes == null ? null : objectMapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param object对象
     * @param type     类型引用对象
     * @param <T>      对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(Object o, Class<T> type) {
        try {
            return o == null ? null : objectMapper.convertValue(o, type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象反序列化
     *
     * @param object对象
     * @param type     类型引用对象
     * @param <T>      对象类型
     *
     * @return 对象实例
     */
    public <T> T deserialize(Object o, TypeReference<T> type) {
        try {
            return o == null ? null : objectMapper.convertValue(o, type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
