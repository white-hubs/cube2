package com.guhecloud.platform.license.filter;

import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.util.HttpUtils;
import com.guhecloud.platform.license.LicenseVerifyHolder;
import com.guhecloud.platform.license.verify.LicenseVerify;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class LicenseValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 在这里执行许可证验证逻辑
        LicenseVerify licenseVerify = LicenseVerifyHolder.getLicenseVerify();
        if (Objects.isNull((licenseVerify)) || request.getRequestURI().startsWith("/public/license") || licenseVerify.verify()) {
            // 许可证有效，继续过滤器链
            filterChain.doFilter(request, response);
        } else {
            // 许可证无效，返回错误响应
            HttpUtils.writeUnifiedResponse(response, ErrorResponse.ofSimpleMessage(HttpStatus.UNAUTHORIZED.value(), "License is invalid or expired."));
        }
    }
}
