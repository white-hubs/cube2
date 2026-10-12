package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.client;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.client.ClientUserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class ClientUserTokenConverter implements AuthenticationConverter {

    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("client_credentials_user");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        // 参数提取
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        var username = parameters.get(PARAM_USERNAME_KEY);
        // 认证身份
        return new ClientUserAuthenticationToken(username, GRANT_TYPE, SecurityContextHolder.getContext()
                .getAuthentication(), null);
    }
}
