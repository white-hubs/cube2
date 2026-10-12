package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.exchange;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.exchange.KaiHeExchangeAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class KaiHeExchangeTokenConverter implements AuthenticationConverter {
    private final String PARAM_KAIHETOKEN = "kaiHeToken";
    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("exchange_token");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        var token = parameters.get(PARAM_KAIHETOKEN);
        return new KaiHeExchangeAuthenticationToken(token, GRANT_TYPE, SecurityContextHolder.getContext().getAuthentication(), null);
    }
}
