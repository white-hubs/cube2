package com.guhecloud.cube.base.openapi.server.controller;

import com.guhecloud.cube.base.openapi.server.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/openapi")
public class OpenAPIController {

    public final ProxyService proxyService;

    public OpenAPIController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/{application}/**")
    public void proxy(@PathVariable("application") String application, HttpServletRequest request, HttpServletResponse response) {
        var requestUri = request.getRequestURI();
        var prefix = "/openapi/" + application + "/";
        var path = requestUri.substring(requestUri.indexOf(prefix) + prefix.length());
        proxyService.proxy(application, path, request, response);
    }
}
