package com.guhecloud.cube.gateway.filter;

import com.guhecloud.cube.gateway.utils.ReactiveAddrUtil;
import eu.bitwalker.useragentutils.UserAgent;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
public class RequestStatisticsFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_ID_HEADER = "request-id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        UserAgent userAgent = UserAgent.parseUserAgentString(headers.get("User-Agent"));
        // 埋点
        PointUtil.debug("1", "request-statistics",
                        "ip=" + ReactiveAddrUtil.getRemoteAddr(request) + "&browser="
                        + getBrowser(userAgent.getBrowser().name()) + "&operatingSystem="
                        + getOperatingSystem(userAgent.getOperatingSystem().name()));

        // TODO 改为时间加随机数
        // requestId for tracing
        exchange.getRequest().mutate().header(REQUEST_ID_HEADER, UUID.randomUUID().toString()).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getBrowser(String browser) {
        if (StringUtils.isNotEmpty(browser)) {
            if (browser.contains("CHROME")) {
                return "CHROME";
            } else if (browser.contains("FIREFOX")) {
                return "FIREFOX";
            } else if (browser.contains("SAFARI")) {
                return "SAFARI";
            } else if (browser.contains("EDGE")) {
                return "EDGE";
            }
        }
        return browser;
    }

    private String getOperatingSystem(String operatingSystem) {
        if (StringUtils.isNotEmpty(operatingSystem)) {
            if (operatingSystem.contains("MAC_OS_X")) {
                return "MAC_OS_X";
            } else if (operatingSystem.contains("ANDROID")) {
                return "ANDROID";
            }
        }
        return operatingSystem;
    }
}
