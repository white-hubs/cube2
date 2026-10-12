package com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

@Getter
public class MSAccessAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String code;
    private final String redirectUri;

    public MSAccessAuthenticationToken(String code, String redirectUri,
                                       AuthorizationGrantType authorizationGrantType,
                                       Authentication clientPrincipal,
                                       Map<String, Object> additionalParameters
                                      ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
