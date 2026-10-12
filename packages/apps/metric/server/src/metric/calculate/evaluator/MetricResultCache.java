package com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator;

import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 指标结果缓存管理器（单例模式）
 * 作用：缓存相同 metricKey + param 的指标结果，避免重复计算
 */
@Component
public class MetricResultCache {
    
    private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    /**
     * 获取缓存的指标结果（不存在则返回null）
     */
    public Object get(String metricKey, MetricParam param) {
        String cacheKey = generateKey(metricKey, param);
        return cache.get(cacheKey);
    }

    /**
     * 缓存指标结果
     */
    public void put(String metricKey, MetricParam param, Object resultData) {
        String cacheKey = generateKey(metricKey, param);
        cache.put(cacheKey, resultData);
    }

    /**
     * 生成唯一缓存键（需确保param参数可序列化）
     */
    private String generateKey(String metricKey, MetricParam param) {
        return metricKey + "_" + param.hashCode();
    }
}
