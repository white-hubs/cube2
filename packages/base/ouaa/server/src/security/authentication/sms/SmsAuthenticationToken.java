package com.guhecloud.cube.biz.ouaa.server.security.authentication.sms;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;

public class SmsAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    @Getter
    private final String mobile;
    @Getter
    private final String smsCode;

    public SmsAuthenticationToken(String mobile, String smsCode, AuthorizationGrantType authorizationGrantType,
                                  Authentication clientPrincipal
                                 ) {
        super(authorizationGrantType, clientPrincipal, null);
        this.mobile = mobile;
        this.smsCode = smsCode;
    }

}
