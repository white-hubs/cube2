package com.guhecloud.cube.customization.xialing.server.communal.service.impl;

import com.guhecloud.cube.customization.xialing.server.communal.service.XiaNingVisualizationService;
import com.guhecloud.cube.customization.xialing.server.drone.auth.XianingCommandDispatchSsoTokenProvider;
import com.guhecloud.cube.customization.xialing.server.drone.auth.XianingSsoTokenProvider;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class XiaNingVisualizationServiceImpl implements XiaNingVisualizationService {
    private static final String DRONE_WEB_PATH = "/webuver/";
    private static final String BEARER_PREFIX = "Bearer ";

    private final XianingSsoTokenProvider ssoTokenProvider;
    private final XianingCommandDispatchSsoTokenProvider commandDispatchSsoTokenProvider;

    /**
     * 获取无人机可视化免密跳转地址。
     *
     * @return 无人机可视化免密跳转地址
     */
    @Override
    public String droneSsoUrl() {
        return droneSsoUrl(ssoTokenProvider.getFreshToken(), ssoTokenProvider.getTenantId(), ssoTokenProvider.getBaseUrl());
    }

    /**
     * 获取指挥调度入口跳转无人机平台的免密跳转地址。
     *
     * @return 指挥调度入口无人机平台免密跳转地址
     */
    @Override
    public String commandDispatchSsoUrl() {
        return droneSsoUrl(commandDispatchSsoTokenProvider.getFreshToken(), commandDispatchSsoTokenProvider.getTenantId(),
                           commandDispatchSsoTokenProvider.getBaseUrl());
    }

    private String droneSsoUrl(String token, String tenantId, String baseUrl) {
        String jumpInfo = "token=" + bearerToken(token) + "&Tenantid=" + tenantId;
        String serverCode = Base64.getEncoder().encodeToString(jumpInfo.getBytes(StandardCharsets.UTF_8));
        return UriComponentsBuilder.fromUriString(baseUrl + DRONE_WEB_PATH)
                .queryParam("ser", serverCode)
                .build()
                .encode()
                .toUriString();
    }

    private String bearerToken(String token) {
        Assert.isParameterError(StringUtils.isBlank(token), "霞凝无人机 token 为空");
        token = token.trim();
        if (StringUtils.startsWithIgnoreCase(token, BEARER_PREFIX)) {
            return token;
        }
        return BEARER_PREFIX + token;
    }
}
