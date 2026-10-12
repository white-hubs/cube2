package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.config.GhyHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyLoginResponse;
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
import org.springframework.util.DigestUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractGhyRequest extends AbstractRequest {

    private static final String GHY_STREAM_LOGIN_TOKEN_REDIS_KEY = "GHY_STREAM_LOGIN_TOKEN";

    @JsonIgnore
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = GHY_STREAM_LOGIN_TOKEN_REDIS_KEY + ":" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            GhyLoginResponse loginResponse = getAuthorization();
            redisTemplate.opsForValue().set(key, loginResponse.getAccessToken(), 400, TimeUnit.SECONDS);
            token = loginResponse.getAccessToken();
        }
        return token;
    }

    @Override
    public void addCustomHttpHeaders(HttpHeaders headers) {
        String token = getToken();
        if (StringUtils.isNotBlank(token)) {
            headers.set("access-token", token);
        }
    }

    @JsonIgnore
    public GhyLoginResponse getAuthorization() {
        GhyHttpConfig ghyHttpConfig = getRequestConfig();
        if (Objects.isNull(ghyHttpConfig)) {
            log.warn("获取古河云视频流媒体平台访问配置为空");
        }
        GhyLoginRequest loginRequest = new GhyLoginRequest();
        loginRequest.setUsername(ghyHttpConfig.getUsername());
        loginRequest.setPassword(DigestUtils.md5DigestAsHex(ghyHttpConfig.getPassword().getBytes()));
        ResponseEntity<GhyBaseResponse<GhyLoginResponse>> responseResponseEntity = requester().exchange(
                loginRequest.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GhyBaseResponse<GhyLoginResponse>>() {
                },
                loginRequest.getParamForGet());
        log.debug("获取到的古河云视频流媒体平台认证响应信息{}", responseResponseEntity);
        Assert.isTrue(Objects.nonNull(responseResponseEntity.getBody()), "古河云视频流媒体平台认证失败");
        return responseResponseEntity.getBody().getData();
    }
}
