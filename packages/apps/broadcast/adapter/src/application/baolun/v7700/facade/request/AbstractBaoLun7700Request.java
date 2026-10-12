package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.entity.BaoLunVSevenSevenConfig;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.response.LoginResponse;
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
public abstract class AbstractBaoLun7700Request extends AbstractRequest {

    @JsonIgnore
    public String getAuthToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "BAOLUN:V7700:TOKEN:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            LoginResponse loginResponse = getAuthorization();
            redisTemplate.opsForValue().set(key, loginResponse.getToken(), 60, TimeUnit.MINUTES);
            token = loginResponse.getToken();
        }
        return token;
    }

    @Override
    protected HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = getAuthToken();
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(token)) {
            headers.set("Authorization", token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 获取接口 Authorization
     *
     * @return
     *
     * @throws Exception
     */
    @JsonIgnore
    protected LoginResponse getAuthorization() {
        BaoLunVSevenSevenConfig requestConfig = getRequestConfig();
        if (Objects.isNull(requestConfig)) {
            log.error("获取保伦ITC7700广播鉴权token失败");
        }
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName(requestConfig.getUsername());
        loginRequest.setPassword(requestConfig.getPassword());
        ResponseEntity<LoginResponse> responseResponseEntity = requester().exchange(
                loginRequest.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<LoginResponse>() {
                },
                loginRequest.getParamForGet());
        log.debug("获取到的保伦ITC7700广播登录响应信息{}", responseResponseEntity);
        Assert.isTrue(Objects.nonNull(responseResponseEntity.getBody()), "获取保伦ITC7700广播token失败");
        return responseResponseEntity.getBody();
    }
}
