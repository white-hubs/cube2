package com.guhecloud.cube.biz.ouaa.server.security.authentication.password;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Map;

public class UserAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String username;

    public UserAuthenticationToken(@NotNull AuthorizationGrantType authorizationGrantType, @NotNull String username,
                                   @NotNull Authentication clientPrincipal, @NotNull Map<String, Object> additionalParameters
                                  ) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
