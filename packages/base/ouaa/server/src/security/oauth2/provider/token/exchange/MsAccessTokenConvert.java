package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.exchange;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange.AuthorizationCodeAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class MsAccessTokenConvert implements AuthenticationConverter {

    private static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("ms_accesstoken_code");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        var code = parameters.get(GRANT_TYPE.getValue());
        return new AuthorizationCodeAuthenticationToken(code, GRANT_TYPE, SecurityContextHolder.getContext().getAuthentication(), null);
    }
}
