package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

@Getter
public class KaiHeExchangeAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String token;

    public KaiHeExchangeAuthenticationToken(
            String token,
            AuthorizationGrantType authorizationGrantType,
            Authentication clientPrincipal,
            Map<String, Object> additionalParameters
                                           ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.token = token;
    }
}
