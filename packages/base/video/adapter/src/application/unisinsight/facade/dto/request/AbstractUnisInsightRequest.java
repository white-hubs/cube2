package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.config.UnisInsightHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightAccessTokenResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractUnisInsightRequest extends AbstractRequest {

    private static final String UNIS_INSIGHT_TOKEN_REDIS_KEY = "UNIS_INSIGHT:TOKEN:";

    @JsonIgnore
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = UNIS_INSIGHT_TOKEN_REDIS_KEY + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        UnisInsightAccessTokenResponse tokenResponse = (UnisInsightAccessTokenResponse) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(tokenResponse) || StringUtils.isBlank(tokenResponse.getAccessToken())) {
            UnisInsightAccessTokenResponse loginResponse = getAuthorization();
            long timeout = Objects.nonNull(loginResponse.getExpiresIn()) ? loginResponse.getExpiresIn() / 2 : 3600L;
            redisTemplate.opsForValue().set(key, loginResponse, timeout, TimeUnit.SECONDS);
            return loginResponse.getAccessToken();
        }
        return tokenResponse.getAccessToken();
    }

    @Override
    public void addCustomHttpHeaders(HttpHeaders headers) {
        UnisInsightHttpConfig config = getRequestConfig();
        String token = getToken();
        if (StringUtils.isNotBlank(token)) {
            headers.set("Authorization", token);
        }
        if (Objects.nonNull(config) && StringUtils.isNotBlank(config.getClientId())) {
            headers.set("User", "usercode:" + config.getClientId());
            headers.set("Cookie", "usercode=" + config.getClientId());
        }
    }

    @JsonIgnore
    protected UnisInsightAccessTokenResponse getAuthorization() {
        UnisInsightAccessTokenRequest request = new UnisInsightAccessTokenRequest();
        UnisInsightHttpConfig config = getRequestConfig();
        request.setGrantType("client_credentials");
        request.setClientId(config.getClientId());
        request.setClientSecret(config.getClientSecret());
        request.setFormat("json");
        ResponseEntity<UnisInsightAccessTokenResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UnisInsightAccessTokenResponse>() {
                },
                request.getParamForGet());
        log.debug("获取到的紫光华智智能视觉管理平台认证响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "紫光华智智能视觉管理平台认证失败");
        return response.getBody();
    }
}
