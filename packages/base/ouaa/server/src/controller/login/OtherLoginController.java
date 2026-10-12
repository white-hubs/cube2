package com.guhecloud.cube.biz.ouaa.server.controller.login;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.param.OtherSysLoginParam;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.client.ClientUserTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.RandomUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CLIENT_ID_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CLIENT_SECRET_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_GRANT_TYPE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OtherLoginController {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY = "otherSysLoginSecretId";

    private final UserService userService;

    private final RestTemplate restTemplate;

    @Value(value = "${third-part.auth-server.uri}")
    private String authServerAddr;

    /**
     * 三方系统账号登录
     *
     * @param param param.username为账号
     *
     * @return 鉴权信息
     */
    @PostMapping("/public/other/sys/login")
    public Object otherSysLogin(@RequestBody OtherSysLoginParam param) {
        return accessTokenRequest(param, param.getUsername());
    }

    private Result decryptClient(OtherSysLoginParam param) {
        var secretKey = this.redisTemplate.opsForValue().get(getSecurityKey(param.getSecretId()));
        if (StringUtils.isBlank(secretKey)) {
            throw new InvalidArgumentException("无效的密钥");
        }
        try {
            var clientId = AESUtils.decrypt(secretKey, param.getClient_id());
            var clientSecret = AESUtils.decrypt(secretKey, param.getClient_secret());
            return new Result(clientId, clientSecret);
        } catch (Exception e) {
            throw new InvalidArgumentException("无效的密钥");
        }
    }

    private record Result(String clientId, String clientSecret) {}

    /**
     * 三方系统身份证登录
     *
     * @param param param.username为身份证号
     *
     * @return 鉴权信息
     */
    @PostMapping("/public/other/sys/id-card/login")
    public Object otherSysIdCardLogin(@RequestBody OtherSysLoginParam param) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery()
                                               .eq(User::getIdCard, param.getUsername())
                                               .last(" limit 1 ")
                                      );
        Assert.isNotFound(user, "用户信息不存在!");
        return accessTokenRequest(param, user.getUsername());
    }

    private Object accessTokenRequest(OtherSysLoginParam param, String user) {
        var result = decryptClient(param);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(PARAM_CLIENT_ID_KEY, result.clientId);
        params.add(PARAM_CLIENT_SECRET_KEY, result.clientSecret);
        params.add(PARAM_GRANT_TYPE_KEY, ClientUserTokenConverter.GRANT_TYPE.getValue());
        params.add(PARAM_USERNAME_KEY, user);
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            var response = restTemplate.postForEntity(
                    authServerAddr,
                    new HttpEntity<>(params, headers),
                    Object.class);
            return response.getBody();
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }
    }

    @PostMapping("/public/other/sys/secret")
    public Map<String, Object> getSecret() {
        var secretId = RandomUtils.randomAlphabetic(16);
        var secretKey = RandomUtils.randomAlphabetic(16);
        redisTemplate.opsForValue().set(getSecurityKey(secretId), secretKey, 1, TimeUnit.MINUTES);
        Map<String, String> secret = new HashMap<>();
        secret.put("id", secretId);
        secret.put("key", secretKey);
        return Map.of("data", secret, "code", 200, "message", "success");
    }

    private String getSecurityKey(String secretId) {
        return KEY + ":" + secretId;
    }
}
