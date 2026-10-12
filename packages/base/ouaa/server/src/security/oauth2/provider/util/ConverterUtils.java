package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public final class ConverterUtils {
    private ConverterUtils() {}

    public static boolean unsupportedGrantType(AuthorizationGrantType grantType, HttpServletRequest request) {
        String requestGrantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        return !grantType.getValue().equals(requestGrantType);
    }
}
