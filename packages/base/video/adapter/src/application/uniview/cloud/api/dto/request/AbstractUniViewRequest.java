package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.config.UniViewConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response.UniViewAccessTokenResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response.UniViewResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractUniViewRequest extends AbstractRequest {

    @JsonIgnore
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "UNI_VIEW:TOKEN:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        UniViewAccessTokenResponse uniViewAccessTokenResponse = (UniViewAccessTokenResponse) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(uniViewAccessTokenResponse)) {
            UniViewAccessTokenResponse loginResponse = getAuthorization();
            redisTemplate.opsForValue().set(key, loginResponse, loginResponse.getExpireTime() / 2, TimeUnit.SECONDS);
            return loginResponse.getAccessToken();
        }
        return uniViewAccessTokenResponse.getAccessToken();
    }

    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        if (StringUtils.isNotBlank(token)) {
            headers.set("Authorization", token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 获取接口 Authorization
     * accessToken有效期,单位秒，默认7天
     *
     * @return
     *
     * @throws Exception
     */
    @JsonIgnore
    protected UniViewAccessTokenResponse getAuthorization() {
        UniViewConfig config = getRequestConfig();
        UniViewAccessTokenRequest request = new UniViewAccessTokenRequest();
        request.setAppId(config.getAppId());
        request.setSecretKey(config.getSecretKey());
        ResponseEntity<UniViewResponse<UniViewAccessTokenResponse>> getTokenResponseEntity = requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<UniViewResponse<UniViewAccessTokenResponse>>() {
                });
        log.debug("获取到的宇视平台登录鉴权响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "获取到的宇视平台登录鉴权响应信息失败");
        return getTokenResponseEntity.getBody().getData();
    }
}
