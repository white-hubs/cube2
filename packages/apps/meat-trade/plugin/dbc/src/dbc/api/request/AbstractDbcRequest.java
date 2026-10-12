package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.config.DbcPorkArrangePushConfig;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BaseResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.LoginResponse;
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
public abstract class AbstractDbcRequest extends AbstractRequest {

    @JsonIgnore
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "DBC:MEATTRADE:TOKEN:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            LoginResponse loginResponse = getAuthorization();
            redisTemplate.opsForValue().set(key, loginResponse.getAccess_token(), loginResponse.getExpires_in(), TimeUnit.SECONDS);
            token = loginResponse.getAccess_token();
        }
        return "Bearer " + token;
    }

    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(token)) {
            headers.set("Authorization", token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 获取接口 Authorization，有效期 5 分钟 Authorization
     *
     * @return
     *
     * @throws Exception
     */
    @JsonIgnore
    protected LoginResponse getAuthorization() {
        DbcPorkArrangePushConfig requestConfig = getRequestConfig();
        if (Objects.isNull(requestConfig)) {
            log.error("获取大白菜滑轨鉴权token失败");
        }
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(requestConfig.getUsername());
        loginRequest.setPassword(requestConfig.getPassword());
        ResponseEntity<BaseResponse<LoginResponse>> responseResponseEntity = requester().exchange(
                loginRequest.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<LoginResponse>>() {
                },
                loginRequest.getParamForGet());
        log.debug("获取到的大白菜滑轨登录响应信息{}", responseResponseEntity);
        Assert.isTrue(Objects.nonNull(responseResponseEntity.getBody()), "获取大白菜token失败");
        return responseResponseEntity.getBody().getData();
    }
}
