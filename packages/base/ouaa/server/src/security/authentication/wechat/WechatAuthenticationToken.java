package com.guhecloud.cube.biz.ouaa.server.security.authentication.wechat;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;

@Getter
public class WechatAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String token;
    private final String mobile;
    private final String iv;

    public WechatAuthenticationToken(String token, String mobile,
                                     String iv,
                                     AuthorizationGrantType authorizationGrantType,
                                     Authentication clientPrincipal
                                    ) {
        super(authorizationGrantType, clientPrincipal, null);
        this.token = token;
        this.mobile = mobile;
        this.iv = iv;
    }
}
