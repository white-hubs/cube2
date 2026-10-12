package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.wechat;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.wechat.WechatAuthenticationToken;
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

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_IV_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_MOBILE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_WECHAT_TOKEN_KEY;

@Component
public class WechatTokenConverter implements AuthenticationConverter {

    private static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("wechat");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        return authentication(parameters);
    }

    protected Authentication authentication(Map<String, String> parameters) {
        var clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        var token = parameters.get(PARAM_WECHAT_TOKEN_KEY);
        var mobile = parameters.get(PARAM_MOBILE_KEY);
        var iv = parameters.get(PARAM_IV_KEY);
        if (StringUtils.containsEmpty(token, mobile)) {
            throw new OAuth2AuthenticationException("authentication or mobile is empty");
        }
        iv = ObjectUtils.ifNull(iv, "").trim();
        return new WechatAuthenticationToken(token, mobile, iv, GRANT_TYPE, clientPrincipal);
    }

}
