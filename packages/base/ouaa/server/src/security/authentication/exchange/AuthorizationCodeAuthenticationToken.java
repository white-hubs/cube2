package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

@Getter
public class AuthorizationCodeAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String code;

    public AuthorizationCodeAuthenticationToken(
            String code,
            AuthorizationGrantType authorizationGrantType,
            Authentication clientPrincipal,
            Map<String, Object> additionalParameters
                                               ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.code = code;
    }
}
