package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.config.DaHuaConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaPublicKeyResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaRefreshTokenResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaTokenResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.enumeration.DaHuaAuth2CodeEnum;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.util.DaHuaRSAUtil;
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
public abstract class AbstractDaHuaRequest extends AbstractRequest {

    @JsonIgnore
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "DA_HUA:ICC:TOKEN:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        DaHuaTokenResponse daHuaTokenResponse = (DaHuaTokenResponse) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(daHuaTokenResponse)) {
            DaHuaTokenResponse loginResponse = getAuthorization();
            redisTemplate.opsForValue().set(key, loginResponse, loginResponse.getExpiresIn() / 2, TimeUnit.SECONDS);
            return loginResponse.getAccessToken();
        }
        return daHuaTokenResponse.getAccessToken();
    }

    /**
     * 刷新tokne
     *
     * @return
     */
    @JsonIgnore
    public String refreshToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "DA_HUA:ICC:TOKEN:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        DaHuaTokenResponse daHuaTokenResponse = (DaHuaTokenResponse) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(daHuaTokenResponse)) {
            log.warn("da_hua access token cache is null, tenant:{}", tenant);
            return StringUtils.EMPTY_STRING;
        }
        DaHuaRefreshTokenResponse refreshTokenResponse = refreshToken(daHuaTokenResponse.getRefreshToken());
        DaHuaTokenResponse tokenResponse = DaHuaTokenResponse.builder()
                .magicId(refreshTokenResponse.getMagicId())
                .scope(refreshTokenResponse.getScope())
                .expiresIn(refreshTokenResponse.getExpiresIn())
                .accessToken(refreshTokenResponse.getAccessToken())
                .refreshToken(refreshTokenResponse.getRefreshToken())
                .tokenType(refreshTokenResponse.getTokenType())
                .userId(refreshTokenResponse.getUserId()).build();
        redisTemplate.opsForValue().set(key, tokenResponse, tokenResponse.getExpiresIn() / 2, TimeUnit.SECONDS);
        return tokenResponse.getAccessToken();
    }

    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(token)) {
            headers.set("Authorization", "bearer " + token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 获取公钥
     *
     * @return
     */
    @JsonIgnore
    protected DaHuaPublicKeyResponse getRequestPublicKey() {
        DaHuaGetPublicKeyRequest publicKeyRequest = new DaHuaGetPublicKeyRequest();
        ResponseEntity<DaHuaResponse<DaHuaPublicKeyResponse>> responseResponseEntity = requester().exchange(
                publicKeyRequest.getRequestUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<DaHuaResponse<DaHuaPublicKeyResponse>>() {
                },
                publicKeyRequest.getParamForGet());
        log.debug("获取到的大华icc平台登录公钥响应信息{}", responseResponseEntity);
        Assert.isTrue(Objects.nonNull(responseResponseEntity.getBody()), "获取到的大华icc平台登录公钥响应信息失败");
        DaHuaResponse<DaHuaPublicKeyResponse> publicKeyResponse = responseResponseEntity.getBody();
        return publicKeyResponse.getData();
    }

    /**
     * 获取接口 Authorization
     * access_token有效期,单位秒，默认2h
     *
     * @return
     *
     * @throws Exception
     */
    @JsonIgnore
    protected DaHuaTokenResponse getAuthorization() {
        DaHuaConfig config = getRequestConfig();
        DaHuaPublicKeyResponse requestPublicKey = getRequestPublicKey();
        DaHuaGetTokenRequest request = new DaHuaGetTokenRequest();
        request.setGrantType(DaHuaAuth2CodeEnum.PASSWORD.getCode());
        request.setUrl("https://172.16.9.10:443");
        request.setUsername(config.getUsername());
        request.setPassword(DaHuaRSAUtil.encrypt(config.getPassword(), requestPublicKey.getPublicKey()));
        request.setClientId(config.getClientId());
        request.setClientSecret(config.getClientSecret());
        request.setPublicKey(requestPublicKey.getPublicKey());
        request.setCode(config.getCode());
        request.setVerifyCodeFlag(config.getVerifyCodeFlag());
        ResponseEntity<DaHuaResponse<DaHuaTokenResponse>> getTokenResponseEntity = requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaTokenResponse>>() {
                });
        log.debug("获取到的大华icc平台登录鉴权响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "获取到的大华icc平台登录鉴权响应信息失败");
        return getTokenResponseEntity.getBody().getData();
    }

    /**
     * 刷新token请求
     *
     * @param refreshToken
     *
     * @return
     */
    @JsonIgnore
    protected DaHuaRefreshTokenResponse refreshToken(String refreshToken) {
        DaHuaConfig config = getRequestConfig();
        DaHuaRefreshTokenRequest request = new DaHuaRefreshTokenRequest();
        request.setUrl(config.getSchema() + config.getHost());
        request.setGrantType(DaHuaAuth2CodeEnum.REFRESH_TOKEN.getCode());
        request.setClientId(config.getClientId());
        request.setClientSecret(config.getClientSecret());
        request.setRefreshToken(refreshToken);
        ResponseEntity<DaHuaResponse<DaHuaRefreshTokenResponse>> refreshTokenResponse = requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaRefreshTokenResponse>>() {
                });
        log.debug("大华icc平台刷新token响应信息{}", refreshTokenResponse);
        Assert.isTrue(Objects.nonNull(refreshTokenResponse.getBody()), "大华icc平台刷新token失败");
        return refreshTokenResponse.getBody().getData();
    }
}
