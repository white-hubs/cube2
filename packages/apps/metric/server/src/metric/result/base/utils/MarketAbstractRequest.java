package com.guhecloud.cube.biz.metric.server.metric.result.base.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.client.vo.MarketConfigVO;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.request.BaseHttp;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 11:32
 * @Description: 市场请求
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class MarketAbstractRequest extends BaseHttp {
    private static final String MARKET_TOKEN = "MARKET_TOKEN";
    private MarketConfigVO marketConfigVO;

    public MarketAbstractRequest(MarketConfigVO marketConfigVO) {
        this.marketConfigVO = marketConfigVO;
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getTokenClient());
        headers.set("tenant", marketConfigVO.getMarketTenant().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @JsonIgnore
    public HttpEntity<String> getHttpEntity(Object paramObject) {
        return new HttpEntity<>(JsonUtils.serialize(paramObject), this.getHttpHeaders());
    }

    @JsonIgnore
    public MultiValueMap<String, String> getParamForGet(Object urlParams) {
        //判断urlParams是否为空
        if (Objects.isNull(urlParams)) {
            return null;
        }
        //创建一个Map用于存储参数
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        //获取所有字段以及字段名称以及值
        Class<?> clazz = urlParams.getClass();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields.length != 0) {
                Arrays.stream(declaredFields).forEach(field -> {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    try {
                        Object o = field.get(urlParams);
                        if (Objects.nonNull(o)) {
                            if (o instanceof Period) {
                                Period period = (Period) o;
                                param.add(fieldName,
                                          DateUtils.format(period.getBegin()) + "," + DateUtils.format(period.getEnd()));
                            } else if (o instanceof Collection) {
                                param.add(fieldName, ((Collection<?>) o).stream().map(Object::toString).collect(Collectors.joining(",")));
                            } else {
                                param.add(fieldName, o.toString());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        log.error("反射违法访问异常：", e);
                    }
                });
            }
            clazz = clazz.getSuperclass();
        }
        return param;
    }

    private String getTokenClient() {
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        Object marketToken = redisTemplate.opsForHash().get(MARKET_TOKEN, marketConfigVO.getId().toString());
        if (Objects.nonNull(marketToken)) {
            return marketToken.toString();
        }
        String url = (marketConfigVO.getUrl().endsWith("/") ? marketConfigVO.getUrl() : marketConfigVO.getUrl() + "/")
                     + "api/ouaa/oauth/token";
        MultiValueMap<String, Object> loginParam = new LinkedMultiValueMap<>();
        loginParam.add("grant_type", "client_credentials");
        loginParam.add("client_id", marketConfigVO.getAccount());
        loginParam.add("client_secret", marketConfigVO.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.set("terminal", "bimops-manage-web");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try {
            ResponseEntity<String> responseEntity = this.requester().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(loginParam, headers),
                    new ParameterizedTypeReference<String>() {
                    });
            Map<String, Object> responseMap = JsonUtils.deserialize(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Object accessToken = responseMap.get("access_token");
            redisTemplate.opsForHash().put(MARKET_TOKEN, marketConfigVO.getId().toString(), accessToken.toString());
            redisTemplate.expire(MARKET_TOKEN, 30, TimeUnit.MINUTES);
            return accessToken.toString();
        } catch (Exception e) {
            log.error("获取市场认证信息失败：", e);
            return null;
        }
    }

    public String getRequestUrl(String url, Object urlParams) {
        url = (marketConfigVO.getUrl().endsWith("/") ? marketConfigVO.getUrl() + "api" : marketConfigVO.getUrl() + "/api")
              + url;
        if (Objects.nonNull(urlParams)) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            return builder.queryParams(this.getParamForGet(urlParams)).build().toUriString();
        }
        return url;
    }
}
