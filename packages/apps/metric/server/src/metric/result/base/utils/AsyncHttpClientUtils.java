package com.guhecloud.cube.biz.metric.server.metric.result.base.utils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.client.SafetyMarketConfigClient;
import com.guhecloud.cube.base.dataintegrate.client.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.client.vo.MarketConfigVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 11:10
 * @Description:
 */
@Slf4j
@Component
public class AsyncHttpClientUtils {

    private static final String MARKET_CONFIG_CACHE = "MARKET_CONFIG_CACHE";

    private final SafetyMarketConfigClient safetyMarketConfigClient;
    @Resource(name = "asyncMetricExecutor")
    private ThreadPoolTaskExecutor asyncMetricExecutor;

    public AsyncHttpClientUtils(SafetyMarketConfigClient safetyMarketConfigClient) {
        this.safetyMarketConfigClient = safetyMarketConfigClient;
    }

    /**
     * 获取所有市场指标
     */
    public <T> List<MarketMetricResult<T>> getAllMarketMetrics(String url, HttpMethod method, Object bodyParam, Object urlParams,
                                                               TypeReference<T> tTypeReference
                                                              ) {
        List<MarketConfigVO> marketConfigClientList = this.getAllMarketConfigs();
        if (CollectionUtils.isEmpty(marketConfigClientList)) {
            return null;
        }
        List<MarketMetricResult<T>> result = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (MarketConfigVO marketConfigVO : marketConfigClientList) {
            completableFutures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    MarketAbstractRequest marketAbstractRequest = new MarketAbstractRequest(marketConfigVO);
                    ResponseEntity<String> responseEntity = marketAbstractRequest.requester().exchange(
                            marketAbstractRequest.getRequestUrl(url, urlParams),
                            method,
                            marketAbstractRequest.getHttpEntity(bodyParam),
                            new ParameterizedTypeReference<String>() {
                            });
                    MarketMetricResult<T> marketMetricResult = new MarketMetricResult<>();
                    marketMetricResult.setMarketId(marketConfigVO.getId());
                    marketMetricResult.setMarketCode(marketConfigVO.getMarketCode());
                    marketMetricResult.setMarketName(marketConfigVO.getMarketName());
                    JSONObject jsonObject = JsonUtils.deserialize(responseEntity.getBody(), JSONObject.class);
                    if (jsonObject.containsKey("data")) {
                        try {
                            marketMetricResult.setMetricStruct(JsonUtils.deserialize(jsonObject.getJSONObject("data"), tTypeReference));
                        } catch (Exception e) {
                            marketMetricResult.setMetricStruct(JsonUtils.deserialize(jsonObject.getJSONArray("data"), tTypeReference));
                        }

                    } else {
                        marketMetricResult.setMetricStruct(JsonUtils.deserialize(responseEntity.getBody(), tTypeReference));
                    }
                    return marketMetricResult;
                } catch (Exception e) {
                    log.error("获取{}市场统计失败", marketConfigVO.getMarketName(), e);
                }
                return null;
            }, asyncMetricExecutor).thenAccept(result::add));
        }
        //所有任务结束
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).exceptionally(e -> {
            log.error("获取市场统计失败", e);
            throw new CompletionException(e);
        }).join();
        result.removeIf(Objects::isNull);
        return result;
    }

    private List<MarketConfigVO> getAllMarketConfigs() {
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        Object object = redisTemplate.opsForValue().get(MARKET_CONFIG_CACHE);
        if (Objects.nonNull(object)) {
            return JsonUtils.deserialize(object.toString(), new TypeReference<List<MarketConfigVO>>() {
            });
        }
        List<MarketConfigVO> marketConfigClientList = safetyMarketConfigClient.getMarketConfigList(new MarketConfigQueryParam());
        if (CollectionUtils.isNotEmpty(marketConfigClientList)) {
            redisTemplate.opsForValue()
                    .set(MARKET_CONFIG_CACHE, JsonUtils.serialize(marketConfigClientList), 30, TimeUnit.MINUTES);
        }
        return marketConfigClientList;
    }
}
