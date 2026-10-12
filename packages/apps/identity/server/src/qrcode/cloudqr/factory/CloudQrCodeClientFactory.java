package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.factory;

import com.guhecloud.cube.base.cloud.qr.code.sdk.CloudQrCodeClient;
import com.guhecloud.cube.base.cloud.qr.code.sdk.config.CloudQrCodeSdkConfig;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeConfigService;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 云二维码Client工厂
 * 职责：根据租户配置创建CloudQrCodeClient实例
 *
 * @author system
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeClientFactory {

    private final CloudQrCodeConfigService configService;

    /**
     * 创建当前租户的CloudQrCodeClient
     *
     * @return CloudQrCodeClient实例，未配置返回null
     */
    public CloudQrCodeClient createClient() {
        var config = configService.getEnabledConfig();
        if (config == null) {
            log.debug("当前租户未启用云二维码配置");
            return null;
        }
        return createClient(config);
    }

    /**
     * 根据配置创建CloudQrCodeClient
     *
     * @param config 云二维码配置
     *
     * @return CloudQrCodeClient实例
     */
    public CloudQrCodeClient createClient(CloudQrCodeConfigPO config) {
        if (config == null || !config.getEnabled()) {
            log.warn("云二维码配置为空或未启用");
            return null;
        }

        if (StringUtils.isBlank(config.getAppId())
            || StringUtils.isBlank(config.getAppSecret())
            || StringUtils.isBlank(config.getServerUrl())) {
            log.warn("云二维码配置不完整，appId={}, serverUrl={}",
                     config.getAppId(), config.getServerUrl());
            return null;
        }
        var sdkConfig = new CloudQrCodeSdkConfig(
                config.getAppId(),
                config.getAppSecret(),
                config.getServerUrl()
        );

        log.debug("创建云二维码客户端，租户={}, serverUrl={}",
                  config.getTenant(), config.getServerUrl());

        return new CloudQrCodeClient(sdkConfig);
    }

    /**
     * 检查当前租户是否启用云二维码
     *
     * @return true-已启用，false-未启用
     */
    public boolean isEnabled() {
        var config = configService.getEnabledConfig();
        return config != null && config.getEnabled();
    }
}
