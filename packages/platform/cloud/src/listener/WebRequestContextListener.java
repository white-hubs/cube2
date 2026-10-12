package com.guhecloud.platform.cloud.listener;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.http.HttpConstants;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.HttpUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

@WebListener
public class WebRequestContextListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent e) {
        SessionContextHolder.setRequester(this.buildRequester((HttpServletRequest) e.getServletRequest()));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        AESUtils.clear();
        SecurityContextHolder.clearSecurityContext();
        SessionContextHolder.clearSessionContext();
    }

    private Requester buildRequester(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String host = HttpUtils.getRequestAddress(request);
        String domain = HttpUtils.getRequestDomain(request);
        String authorization = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_AUTHORIZATION), StringUtils.EMPTY_STRING).trim();
        Integer tenant = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_TENANT), t -> Integer.parseInt(t.trim()), () -> null);
        String terminal = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_TERMINAL), StringUtils.EMPTY_STRING).trim();
        String version = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_VERSION), StringUtils.EMPTY_STRING).trim();
        String signature = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_SIGNATURE), StringUtils.EMPTY_STRING).trim();
        String permission = ObjectUtils.ifNull(request.getHeader(HttpConstants.HEADER_PERMISSION), StringUtils.EMPTY_STRING).trim();
        Requester requester = Requester.builder().uri(uri).host(host).domain(domain).authorization(authorization).tenant(tenant)
                .terminal(terminal).version(version).signature(signature).permission(permission).build();
        return requester;
    }

}
