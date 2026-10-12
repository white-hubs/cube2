package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.service.login.MSOauthService;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class MSAccessAuthenticationProvider extends AbstractOAuth2UserProvider<MSAccessAuthenticationToken> {

    private final MSOauthService msOauthService;

    public MSAccessAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          OAuth2AuthorizationService authorizationService, MSOauthService msOauthService
                                         ) {
        super(tokenGenerator, authorizationService);
        this.msOauthService = msOauthService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (MSAccessAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(MSAccessAuthenticationToken authentication) {
        var code = authentication.getCode();
        var redirectUri = authentication.getRedirectUri();
        var user = msOauthService.getAccessToken(code, redirectUri);
        if (ObjectUtils.isEmpty(user)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户不存在", null));
        }
        return convertToken(authentication.getGrantType(), (OAuth2ClientAuthenticationToken) authentication.getPrincipal(), user);
    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken,
                                                 User userDetails
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }
}
