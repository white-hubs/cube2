package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TENANT_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TERMINAL_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class AuthorizationCodeAuthenticationProvider extends AbstractOAuth2UserProvider<AuthorizationCodeAuthenticationToken> {

    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthorizationCodeAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                                   OAuth2AuthorizationService authorizationService,
                                                   UserDetailsService userDetailsService, RedisTemplate<String, Object> redisTemplate
                                                  ) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthorizationCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(AuthorizationCodeAuthenticationToken authentication) {
        var redisKey = "secret:" + authentication.getCode();
        var cacheUserInfo = (String) redisTemplate.opsForValue().get(redisKey);
        Map<String, Object> userInfo;
        try {
            userInfo = JsonUtils.deserializeToBean(cacheUserInfo, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "秘钥解析失败，请重新获取", null));
        }
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "秘钥失效，请重新获取", null));
        }
        redisTemplate.delete(redisKey);
        var user = userDetailsService.loadUserByUsername(userInfo.get(PARAM_USERNAME_KEY).toString());
        if (ObjectUtils.isEmpty(user)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户不存在", null));
        }
        return convertToken(authentication.getGrantType(), (OAuth2ClientAuthenticationToken) authentication.getPrincipal(), user, userInfo);
    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken,
                                                 UserDetails userDetails,
                                                 Map<String, Object> userInfo
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        additionalParameters.put(PARAM_TENANT_KEY, userInfo.get(PARAM_TENANT_KEY));
        additionalParameters.put(PARAM_TERMINAL_KEY, userInfo.get(PARAM_TERMINAL_KEY));
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }
}
