package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudQrCodeQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudQrRouteQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * 云二维码路由跳转控制器
 *
 * @author system
 */
@Slf4j
@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrRouteController {

    private final CloudQrRouteQueryService cloudQrRouteQueryService;
    private final CloudQrCodeQueryService cloudQrCodeQueryService;

    /**
     * 根据短码进行路由跳转或显示文本
     * <p>
     * 访问路径：/qr/{shortCode}
     * - mode=ROUTE: 重定向到配置的目标URL
     * - mode=TEXT: 显示文本内容
     *
     * @param shortCode 二维码短码
     *
     * @return 重定向视图或文本响应
     */
    @GetMapping("/{shortCode}")
    public Object redirect(@PathVariable String shortCode) {
        log.info("收到二维码访问请求，短码: {}", shortCode);
        SessionContextHolder.requestTenant(0);
        // 查询二维码信息
        var qrCode = cloudQrCodeQueryService.getByShortCode(shortCode);
        var mode = qrCode.getMode();

        // TEXT模式：直接返回文本内容
        if ("TEXT".equals(mode)) {
            log.info("二维码显示文本，短码: {}", shortCode);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(qrCode.getModeContent());
        }

        // ROUTE模式：路由跳转（默认）
        var qrRoute = qrCode.getQrRoute();
        var targetUrl = qrRoute.getTargetUrl();
        var targetParams = qrRoute.getTargetParams();

        // 如果有参数配置，拼接到URL上
        if (StringUtils.isNotBlank(targetParams)) {
            targetUrl = buildUrlWithParams(targetUrl, targetParams);
        }
        log.info("二维码路由跳转，短码: {}, 目标URL: {}", shortCode, targetUrl);
        // 创建重定向视图，statusCode 302表示临时重定向
        var redirectView = new RedirectView(targetUrl);
        redirectView.setStatusCode(HttpStatus.FOUND);
        return redirectView;
    }

    /**
     * 构建带参数的URL
     *
     * @param baseUrl    基础URL
     * @param paramsJson JSON格式的参数
     *
     * @return 拼接后的URL
     */
    private String buildUrlWithParams(String baseUrl, String paramsJson) {
        try {
            Map<String, Object> params = JsonUtils.deserialize(paramsJson, new TypeReference<>() {});

            var urlBuilder = new StringBuilder(baseUrl);

            // 判断URL是否已有参数
            if (baseUrl.contains("?")) {
                urlBuilder.append("&");
            } else {
                urlBuilder.append("?");
            }

            // 拼接参数
            var first = true;
            if (params != null) {
                for (var entry : params.entrySet()) {
                    if (!first) {
                        urlBuilder.append("&");
                    }
                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                    first = false;
                }
            }
            return urlBuilder.toString();
        } catch (Exception e) {
            log.warn("解析路由参数失败，使用原始URL。参数: {}, 错误: {}", paramsJson, e.getMessage());
            return baseUrl;
        }
    }
}
