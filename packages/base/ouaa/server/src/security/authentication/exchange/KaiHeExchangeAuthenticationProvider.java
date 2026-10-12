package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.util.TokenUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class KaiHeExchangeAuthenticationProvider extends AbstractOAuth2UserProvider<KaiHeExchangeAuthenticationToken> {

    private final OAuth2ResourceServerProperties resourceServerProperties;
    private final UserDetailsService userDetailsService;

    public KaiHeExchangeAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                               OAuth2AuthorizationService authorizationService,
                                               OAuth2ResourceServerProperties resourceServerProperties,
                                               UserDetailsService userDetailsService
                                              ) {
        super(tokenGenerator, authorizationService);
        this.resourceServerProperties = resourceServerProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return KaiHeExchangeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(KaiHeExchangeAuthenticationToken authentication) {
        var decoder = NimbusJwtDecoder.withJwkSetUri(this.resourceServerProperties.getJwt().getJwkSetUri()).build();
        var jwt = decoder.decode(authentication.getToken());
        var account = TokenUtils.getAccount(jwt);
        if (StringUtils.isEmpty(account)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "无效账号", null));
        }
        var user = this.userDetailsService.loadUserByUsername(account);
        if (ObjectUtils.isEmpty(user)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户不存在", null));
        }
        return convertToken(authentication.getGrantType(), (OAuth2ClientAuthenticationToken) authentication.getPrincipal(), user);
    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken,
                                                 UserDetails userDetails
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }
}
