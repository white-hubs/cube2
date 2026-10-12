package com.guhecloud.platform.security.token;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.config.JwtTenantValidatorProperties;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.util.TokenUtils;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtTenantValidator implements OAuth2TokenValidator<Jwt> {

    private final JwtTenantValidatorProperties jwtTenantValidatorProperties;

    public JwtTenantValidator(JwtTenantValidatorProperties jwtTenantValidatorProperties
                             ) {this.jwtTenantValidatorProperties = jwtTenantValidatorProperties;}

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        var terminal = SessionContextHolder.getRequester().getTerminal();
        if (ignoreTerminal(terminal)) {
            return OAuth2TokenValidatorResult.success();
        }
        if (SecurityContextHolder.isClient()) {
            return this.clientJwtTenantValidate(jwt);
        } else {
            return this.userJwtTenantValidate(jwt);
        }
    }

    private OAuth2TokenValidatorResult clientJwtTenantValidate(Jwt jwt) {
        var requester = SessionContextHolder.getRequester();
        var tenants = TokenUtils.getTenants(jwt);
        // 0 租户为特殊，仅限内部服务使用，内部服务（cron、proxy）可以操作任意租户数据，此处验证直接通过
        if (tenants.contains("0")) {
            // 如果请求未指定tenant，则默认为0（仅 tenants.contains("0") 的情况）
            // 如果请求中已指定则不做处理，内部服务允许访问任意租户数据
            if (requester.getTenant() == null) {
                var newRequester = Requester.builder().host(requester.getHost()).domain(requester.getDomain()).tenant(0)
                        .terminal(requester.getTerminal()).version(requester.getVersion()).signature(requester.getSignature()).build();
                SessionContextHolder.setRequester(newRequester);
            }
        } else {
            // 非零租户客户端，强制设置租户为客户端关联的租户（一个客户端只关联一个租户）
            if (requester.getTenant() == null) {
                var newRequester = Requester.builder().host(requester.getHost()).domain(requester.getDomain())
                        .tenant(Integer.valueOf(tenants.getFirst())).terminal(requester.getTerminal()).version(requester.getVersion())
                        .signature(requester.getSignature()).build();
                SessionContextHolder.setRequester(newRequester);
            } else if (!tenants.contains(String.valueOf(requester.getTenant()))) {
                // 验证失败
                var error = new OAuth2Error("tenant illegal", "Principal tenants not contains request tenant", null);
                return OAuth2TokenValidatorResult.failure(error);
            }
        }
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult userJwtTenantValidate(Jwt jwt) {
        var requester = SessionContextHolder.getRequester();
        // 特殊请求不需要请求头带tenant
        var uri = requester.getUri();
        if (ignoreUris(uri)) {
            return OAuth2TokenValidatorResult.success();
        }
        if (ignoreUriPrefix(uri)) {
            return OAuth2TokenValidatorResult.success();
        }
        var tenants = TokenUtils.getTenants(jwt);
        if (tenants.contains("0")) {
            return OAuth2TokenValidatorResult.success();
        }
        // 颁发token时确定的租户中包含此次请求的tenant，则验证通过
        if (tenants.contains(String.valueOf(requester.getTenant()))) {
            return OAuth2TokenValidatorResult.success();
        }
        // 验证失败
        var error = new OAuth2Error("tenant illegal", "Principal tenants not contains request tenant", null);
        return OAuth2TokenValidatorResult.failure(error);
    }

    private Boolean ignoreUris(String uri) {
        if (CollectionUtils.isEmpty(jwtTenantValidatorProperties.getIgnoreUris())) {
            return false;
        }
        if (StringUtils.isBlank(uri)) {
            return false;
        }
        for (var ignoreUri : jwtTenantValidatorProperties.getIgnoreUris()) {
            if (uri.equalsIgnoreCase(ignoreUri)) {
                return true;
            }
        }
        return false;
    }

    private Boolean ignoreUriPrefix(String uri) {
        if (CollectionUtils.isEmpty(jwtTenantValidatorProperties.getIgnoreUriPrefixes())) {
            return false;
        }
        if (StringUtils.isBlank(uri)) {
            return false;
        }
        for (var ignoreUriPrefix : jwtTenantValidatorProperties.getIgnoreUriPrefixes()) {
            if (uri.startsWith(ignoreUriPrefix)) {
                return true;
            }
        }
        return false;
    }

    private Boolean ignoreTerminal(String terminal) {
        if (CollectionUtils.isEmpty(jwtTenantValidatorProperties.getIgnoreTerminals())) {
            return false;
        }
        for (var ignoreTerminal : jwtTenantValidatorProperties.getIgnoreTerminals()) {
            if (terminal.equalsIgnoreCase(ignoreTerminal)) {
                return true;
            }
        }
        return false;
    }
}
