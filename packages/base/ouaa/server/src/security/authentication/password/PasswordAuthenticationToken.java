package com.guhecloud.cube.biz.ouaa.server.security.authentication.password;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Map;

@Getter
public class PasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String password;
    private final String username;
    private final AuthorizationGrantType grantType;

    public PasswordAuthenticationToken(AuthorizationGrantType grantType, String username,
                                       String password, Authentication clientPrincipal,
                                       @Nullable Map<String, Object> additionalParameters
                                      ) {
        super(grantType, clientPrincipal, additionalParameters);
        this.username = username;
        this.password = password;
        this.grantType = grantType;
    }

    public UserAuthenticationToken toUserAuthenticationToken() {
        return new UserAuthenticationToken(grantType, username, (Authentication) this.getPrincipal(), this.getAdditionalParameters());
    }
}
