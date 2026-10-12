package com.guhecloud.platform.base.filter;

import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TraceIdUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * TraceId过滤器
 */
@Component
@Order(1)
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // 从请求头获取traceId，如果没有则生成新的
            String traceId = httpRequest.getHeader(TRACE_ID_HEADER);
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceIdUtils.generateTraceId();
            }

            // 设置到MDC
            TraceIdUtils.setTraceId(traceId);

            // 将traceId添加到响应头
            httpResponse.setHeader(TRACE_ID_HEADER, traceId);

            // 继续处理请求
            chain.doFilter(request, response);

        } finally {
            // 清除MDC，避免内存泄漏
            TraceIdUtils.clearMDC();
        }
    }
}