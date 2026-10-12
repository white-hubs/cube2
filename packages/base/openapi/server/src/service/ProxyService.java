package com.guhecloud.cube.base.openapi.server.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ProxyService {
    void proxy(String application, String path, HttpServletRequest request, HttpServletResponse response);
}
