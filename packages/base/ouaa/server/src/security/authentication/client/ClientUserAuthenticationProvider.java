package com.guhecloud.cube.biz.ouaa.server.security.authentication.client;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class ClientUserAuthenticationProvider extends AbstractOAuth2UserProvider<ClientUserAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    public ClientUserAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                            OAuth2AuthorizationService authorizationService, UserDetailsService userDetailsService
                                           ) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientUserAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(ClientUserAuthenticationToken authentication) {
        var clientAuthenticationToken = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        var userDetails = retrieveUser(authentication.getUsername());
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authentication.getGrantType(), userDetails.getUsername(), clientAuthenticationToken, additionalParameters);
    }

    private UserDetails retrieveUser(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户名不能为空！", null));
        }
        var userDetails = this.userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户不存在！", null));
        }
        return userDetails;
    }
}
