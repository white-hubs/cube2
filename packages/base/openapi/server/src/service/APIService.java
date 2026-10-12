package com.guhecloud.cube.base.openapi.server.service;

import com.guhecloud.cube.base.openapi.server.entity.RegisteredAPI;

import java.util.List;

public interface APIService {
    RegisteredAPI match(String application, String path);

    List<RegisteredAPI> matchAll(String application, String path);
}
