package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.wechat;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.wechat.WechatOpenPlatformAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WechatOpenPlatformTokenConverter implements AuthenticationConverter {

    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("wechat-v2");

    protected Authentication authentication(Map<String, String> parameters) {
        // 参数提取
        String wechatCode = parameters.get("code");

        // 认证身份
        return new WechatOpenPlatformAuthenticationToken(wechatCode, GRANT_TYPE, SecurityContextHolder.getContext()
                .getAuthentication());

    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        return authentication(parameters);
    }
}
