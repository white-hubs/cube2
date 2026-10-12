package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.config.DaHuaConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums.DaHuaAuth2CodeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.qo.DaHuaGetTokenParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.qo.DaHuaQueryParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.qo.DaHuaRefreshTokenParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.util.DaHuaRSAUtil;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaPublicKeyVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaRefreshTokenVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaTokenVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 大华公用的一些http对接接口
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaApi {

    /**
     * 获取公钥
     */
    private static final String PUBLIC_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/public-key";

    /**
     * 获取token
     */
    private static final String GET_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/extend/token";

    /**
     * 刷新token
     */
    private static final String REFRESH_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/extend/refresh/token";

    private final StringRedisTemplate redisTemplate;

    private final RestTemplate restTemplate;
    ;

    /**
     * map转params数据
     */
    private static String mapToParam(Map<String, Object> params) {
        var sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            //去掉最后一个&
            if (!sb.isEmpty()) {
                return sb.substring(0, sb.length() - 1);
            }
        }
        return sb.toString();
    }

    /**
     * token续期
     */
    public void refreshToken(DaHuaConfig config) throws JsonProcessingException {
        var tokenKey = "adapter:door:daHua:token:" + config.getTenant();
        var daHuaTokenObj = redisTemplate.opsForValue().get(tokenKey);
        DaHuaTokenVO daHuaToken = null;
        // 反序列化
        if (StringUtils.isNotEmpty(daHuaTokenObj)) {
            daHuaToken = JsonUtils.deserialize(daHuaTokenObj, DaHuaTokenVO.class);
        }

        var daHuaRefreshTokenVO = new DaHuaRefreshTokenVO();
        //token续期
        if (!ObjectUtils.isEmpty(daHuaToken) && !ObjectUtils.isEmpty(daHuaToken.getAccessToken())) {
            daHuaRefreshTokenVO = this.getRefreshToken(DaHuaRefreshTokenParam.builder()
                                                               .url(config.getHost())
                                                               .grantType(
                                                                       DaHuaAuth2CodeEnum.REFRESH_TOKEN.getCode())
                                                               .clientId(config.getClientId()).clientSecret(
                            config.getClientSecret()).refreshToken(daHuaToken.getRefreshToken()).build());
        }
        //刷新token失败,重新获取token
        if (ObjectUtils.isEmpty(daHuaRefreshTokenVO) || ObjectUtils.isEmpty(daHuaRefreshTokenVO.getAccessToken())) {
            var publicKey = this.getPublicKey(DaHuaQueryParam.builder().host(config.getHost()).build());
            if (ObjectUtils.isEmpty(publicKey)) {
                log.error("大华token续期时获取公钥失败");
                throw new ServiceException("大华token续期时获取公钥失败");
            }
            var jsonStr = JsonUtils.serialize((this.getToken(
                    DaHuaGetTokenParam.builder().grantType(DaHuaAuth2CodeEnum.PASSWORD.getCode())
                            .url(config.getHost())
                            .username(config.getUsername()).password(config.getPassword())
                            .clientId(config.getClientId()).clientSecret(config.getClientSecret())
                            .publicKey(publicKey.getPublicKey()).code(config.getCode())
                            .verifyCodeFlag(config.getVerifyCodeFlag()).build())));
            redisTemplate.opsForValue().set(tokenKey, jsonStr);
            return;
        }
        //正常续期
        redisTemplate.opsForValue().set(tokenKey, JsonUtils.serialize(
                DaHuaTokenVO.builder().magicId(daHuaRefreshTokenVO.getMagicId()).scope(daHuaRefreshTokenVO.getScope())
                        .expiresIn(daHuaRefreshTokenVO.getExpiresIn()).accessToken(daHuaRefreshTokenVO.getAccessToken())
                        .refreshToken(daHuaRefreshTokenVO.getRefreshToken()).tokenType(daHuaRefreshTokenVO.getTokenType())
                        .userId(daHuaRefreshTokenVO.getUserId()).build()));
    }

    /**
     * 获取公钥
     */
    private DaHuaPublicKeyVO getPublicKey(DaHuaQueryParam param) {
        var body = restTemplate.getForObject(param.getHost() + PUBLIC_TOKEN, String.class);
        var daHuaResponse = JsonUtils.deserialize(body, DaHuaResponse.class);
        return ObjectUtils.isEmpty(daHuaResponse.getData()) ? new DaHuaPublicKeyVO()
                                                            : JsonUtils.deserialize(daHuaResponse.getData(), DaHuaPublicKeyVO.class);
    }

    /**
     * 获取access_token认证
     */
    private DaHuaTokenVO getToken(DaHuaGetTokenParam param) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("grant_type", param.getGrantType());
        data.put("client_id", param.getClientId());
        data.put("client_secret", param.getClientSecret());
        data.put("username", param.getUsername());
        data.put("code", param.getCode());
        data.put("verifyCodeFlag", param.getVerifyCodeFlag());
        data.put("public_key", param.getPublicKey());

        try {
            data.put("password", DaHuaRSAUtil.encrypt(param.getPassword(), param.getPublicKey()));
        } catch (Exception e) {
            log.error("大华密码加密失败");
            throw new ServiceException("大华密码加密失败");
        }
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = restTemplate.exchange(param.getUrl() + GET_TOKEN, HttpMethod.POST, new HttpEntity<>(data, httpHeaders), String.class);
        var daHuaResponse = JsonUtils.deserialize(body, DaHuaResponse.class);
        if (ObjectUtils.isEmpty(daHuaResponse.getData())) {
            return new DaHuaTokenVO();
        }
        // 将 Object 转换为 JSON 字符串
        var jsonString = JsonUtils.serialize(daHuaResponse.getData());
        // 使用 FastJSON 将 JSON 字符串解析为 DaHuaTokenVO
        return JsonUtils.deserialize(jsonString, DaHuaTokenVO.class);
    }

    /**
     * 刷新认证接口
     */
    private DaHuaRefreshTokenVO getRefreshToken(DaHuaRefreshTokenParam param) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("grant_type", param.getGrantType());
        data.put("client_id", param.getClientId());
        data.put("client_secret", param.getClientSecret());
        data.put("refresh_token", param.getRefreshToken());
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = restTemplate.exchange(param.getUrl() + REFRESH_TOKEN, HttpMethod.GET, new HttpEntity<>(data, httpHeaders), String.class);
        var daHuaResponse = JsonUtils.deserialize(body, DaHuaResponse.class);
        if (ObjectUtils.isEmpty(daHuaResponse) || ObjectUtils.isEmpty(daHuaResponse.getData())) {
            return new DaHuaRefreshTokenVO();
        }
        // 将 Object 转换为 JSON 字符串
        var jsonString = JsonUtils.serialize(daHuaResponse.getData());
        // 使用 FastJSON 将 JSON 字符串解析为 DaHuaRefreshTokenVO
        return JsonUtils.deserialize(jsonString, DaHuaRefreshTokenVO.class);

    }

    /**
     * get方式 调用需要请求的方法
     */
    public DaHuaResponse daHuaAuthPostApi(String url, Map<String, String> headers, Map<String, Object> params,
                                          DaHuaConfig config
                                         ) throws JsonProcessingException {
        var tokenKey = "adapter:door:daHua:token:" + config.getTenant();
        var daHuaTokenObj = redisTemplate.opsForValue().get(tokenKey);
        // 反序列化
        var daHuaToken = JsonUtils.deserialize(daHuaTokenObj, DaHuaTokenVO.class);

        if (!ObjectUtils.isEmpty(daHuaToken)) {
            headers.put("Authorization", daHuaToken.getTokenType() + " " + daHuaToken.getAccessToken());
            headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }

        var httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        var body = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(params, httpHeaders), String.class)
                .getBody();
        return JsonUtils.deserialize(body, DaHuaResponse.class);
    }
}
