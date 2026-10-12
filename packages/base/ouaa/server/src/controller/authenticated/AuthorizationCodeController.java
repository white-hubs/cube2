package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TENANT_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TERMINAL_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERID_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERTYPE_KEY;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-16  13:48
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationCodeController {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据用户获取短期秘钥
     */
    @GetMapping("/oauth2/getSecretByToken")
    public String token() {
        log.info("开始生成秘钥");
        String secret = java.util.UUID.randomUUID().toString();
        Map<String, Object> tokenMap = new HashMap<>();
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String terminal = SessionContextHolder.getRequester().getTerminal();
        tokenMap.put(PARAM_TENANT_KEY, tenant);
        tokenMap.put(PARAM_USERID_KEY, principal.getId());
        tokenMap.put(PARAM_USERNAME_KEY, principal.getAccount());
        tokenMap.put(PARAM_USERTYPE_KEY, principal.getType());
        tokenMap.put(PARAM_TERMINAL_KEY, terminal);
        var token = JsonUtils.serialize(tokenMap);
        if (token != null) {
            redisTemplate.opsForValue().set(this.getSecretRedisKey(secret), token, 5, TimeUnit.MINUTES);
        }
        log.info("生成秘钥为：{}", secret);
        return secret;
    }

    /**
     * 短期密码key
     */
    private String getSecretRedisKey(String secret) {
        return "secret:" + secret;
    }
}
