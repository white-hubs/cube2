package com.guhecloud.cube.biz.ouaa.server.security.authentication.client;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Map;

@Getter
public class ClientUserAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String username;

    public ClientUserAuthenticationToken(String username, AuthorizationGrantType authorizationGrantType,
                                         Authentication clientPrincipal, @Nullable Map<String, Object> additionalParameters
                                        ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.username = username;
    }
}
