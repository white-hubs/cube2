package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.PasswordAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_PASSWORD_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class PasswordTokenAuthenticationConverter implements AuthenticationConverter {

    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("pwd");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        return authentication(GRANT_TYPE, parameters);
    }

    protected Authentication authentication(AuthorizationGrantType grantType, Map<String, String> parameters) {
        var clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        var username = parameters.get(PARAM_USERNAME_KEY);
        var password = parameters.get(PARAM_PASSWORD_KEY);
        if (StringUtils.containsEmpty(username, password)) {
            throw new OAuth2AuthenticationException("username or password is empty");
        }
        username = ObjectUtils.ifNull(username, "").trim();
        password = ObjectUtils.ifNull(password, "").trim();
        parameters.remove(PARAM_PASSWORD_KEY);
        return new PasswordAuthenticationToken(grantType, username, password, clientPrincipal, (Map<String, Object>) (Map<?, ?>) parameters);
    }
}
