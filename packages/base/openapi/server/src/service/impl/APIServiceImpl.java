package com.guhecloud.cube.base.openapi.server.service.impl;

import com.guhecloud.cube.base.openapi.metadata.enumeration.ResponseType;
import com.guhecloud.cube.base.openapi.server.entity.RegisteredAPI;
import com.guhecloud.cube.base.openapi.server.service.APIService;
import com.guhecloud.cube.base.openapi.server.vo.FunctionMethod;
import com.guhecloud.platform.base.util.AntPathMatcher;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class APIServiceImpl implements APIService {

    private volatile Map<String, List<RegisteredAPI>> registeredAPICache;

    @Override
    public RegisteredAPI match(String application, String path) {
        var apis = matchAll(application, path);
        return apis.getFirst();
    }

    @Override
    public List<RegisteredAPI> matchAll(String application, String path) {
        var apis = loadRegisteredAPIs(application);
        var matcher = new AntPathMatcher();
        var matched = new LinkedList<RegisteredAPI>();
        if (path.charAt(0) != '/') {
            path = "/" + path;
        }
        for (var api : apis) {
            if (matcher.match(api.getPath(), path)) {
                matched.add(api);
            }
        }
        return matched;
    }

    private List<RegisteredAPI> loadRegisteredAPIs(String application) {
        if (registeredAPICache == null) {
            synchronized (this) {
                if (registeredAPICache == null) {
                    // TODO: 待实现
                    registeredAPICache = Map.of("flow", fakeData());
                }
            }
        }
        return registeredAPICache.getOrDefault(application, List.of());
    }

    private List<RegisteredAPI> fakeData() {
        return List.of(
                RegisteredAPI.builder()
                        .name("LegacyFlowProxy")
                        .path("/**")
                        .description("代理所有原 flow 的接口")
                        .appCode("flow")
                        .targetURL("internal://processEngine/{path}")
                        .responseType(ResponseType.TEXT)
                        .afterFC(new FunctionMethod("magic.cube.openapi.builtin.LegacyResponseWrapper", "wrapperResponse"))
                        .build()
                      );
    }
}
