package com.guhecloud.cube.base.openapi.server.service.impl;

import com.guhecloud.cube.base.openapi.server.entity.RegisteredAPI;
import com.guhecloud.cube.base.openapi.server.function.FCEngine;
import com.guhecloud.cube.base.openapi.server.service.APIService;
import com.guhecloud.cube.base.openapi.server.service.ProxyService;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {
    private static final Pattern INTERNAL_PREFIX_PATTERN = Pattern.compile("^internal://([.a-zA-Z0-9_-]+)/.*$");
    private final APIService apiService;
    private final FCEngine fcEngine;
    private final LoadBalancerClient namingService;

    public ProxyServiceImpl(APIService apiService, FCEngine fcEngine, LoadBalancerClient loadBalancerClient) {
        this.apiService = apiService;
        this.fcEngine = fcEngine;
        this.namingService = loadBalancerClient;
    }

    @Override
    public void proxy(String application, String path, HttpServletRequest request, HttpServletResponse response) {
        log.info("ProxyService handle: application={}, path={}", application, path);

        var registeredAPI = apiService.match(application, path);
        if (registeredAPI == null) {
            throw new ResourceNotFoundException("API not found");
        }
        var pathParams = Map.of("path", path);
        var httpRequest = buildProxiedRequest(registeredAPI, request, pathParams);
        sendProxiedRequest(registeredAPI, httpRequest, response);
    }

    private void sendProxiedRequest(RegisteredAPI api, HttpUriRequestBase proxiedRequest, HttpServletResponse response) {
        try (var httpClient = HttpClients.createDefault()) {
            httpClient.execute(proxiedRequest, proxiedResponse -> {
                handleResponse(api, proxiedResponse, response);
                return null;
            });
        } catch (Exception e) {
            log.error("Proxy error", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Proxy error");
            } catch (Exception ignored) {
            }
        }
    }

    private void handleResponse(RegisteredAPI api, ClassicHttpResponse proxiedResponse, HttpServletResponse response) {
        var proxiedResponseEntity = proxiedResponse.getEntity();
        buildResponseHeaders(proxiedResponse, response);
        response.setStatus(proxiedResponse.getCode());
        byte[] body = new byte[0];
        if (proxiedResponseEntity != null) {
            try {
                body = EntityUtils.toByteArray(proxiedResponseEntity);
            } catch (IOException e) {
                throw new ServiceException("Failed to read response body", e);
            }
        }
        try {
            // TODO: 暂时写死只处理 2xx 响应，后续可配置
            if (api.hasAfterFC() && response.getStatus() >= 200 && response.getStatus() < 300) {
                var fcResponse = fcEngine.buildResponse(api.getAfterFC(), new String(body, StandardCharsets.UTF_8));
                body = fcResponse.getBytes(StandardCharsets.UTF_8);
            }
            response.setContentLength(body.length);
            response.getOutputStream().write(body);
        } catch (Exception e) {
            throw new ServiceException("Failed to write response body", e);
        }
    }

    private void buildResponseHeaders(ClassicHttpResponse proxiedResponse, HttpServletResponse response) {
        for (var header : proxiedResponse.getHeaders()) {
            String name = header.getName();
            // 跳过 Transfer-Encoding 相关头，避免 chunked 问题
            if (HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(name)) {
                continue;
            }
            if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
                continue; // 由 servlet 自动处理
            }
            response.setHeader(name, header.getValue());
        }
    }

    private HttpUriRequestBase buildProxiedRequest(RegisteredAPI api, HttpServletRequest request, Map<String, String> pathParams) {
        var targetURL = buildTargetURL(api, pathParams, request.getQueryString());
        // TODO: request.getMethod() 后续换为 api.getTargetMethod()
        var proxiedRequest = new HttpUriRequestBase(request.getMethod(), targetURL);
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var name = headerNames.nextElement();
            if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
                continue; // 由 httpclient 自动处理
            }
            proxiedRequest.setHeader(name, request.getHeader(name));
        }

        try {
            proxiedRequest.setEntity(new InputStreamEntity(request.getInputStream(), ContentType.parse(request.getContentType())));
        } catch (IOException e) {
            throw new ServiceException("Failed to read request body", e);
        }
        return proxiedRequest;
    }

    private URI buildTargetURL(RegisteredAPI api, Map<String, String> pathParams, String queryString) {
        var replacedTargetURL = StringUtils.replaceTemplate(api.getTargetURL(), pathParams, "{", "}");
        var ref = new AtomicReference<>(replacedTargetURL);
        var internalServiceMatcher = INTERNAL_PREFIX_PATTERN.matcher(ref.get());
        if (internalServiceMatcher.find()) {
            var serviceName = internalServiceMatcher.group(1);
            var instance = namingService.choose(serviceName);
            if (instance == null) {
                throw new ResourceNotFoundException("Internal service instance not found: " + serviceName);
            }
            var host = instance.getHost();
            var port = instance.getPort();
            ref.set(ref.get().replace(String.format("internal://%s", serviceName), String.format("http://%s:%s", host, port)));
        }
        return Optional.ofNullable(queryString)
                .map(qs -> URI.create(ref.get() + "?" + qs))
                .orElse(URI.create(ref.get()));
    }
}
