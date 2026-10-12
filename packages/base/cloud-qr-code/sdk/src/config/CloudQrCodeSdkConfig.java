package com.guhecloud.cube.base.cloud.qr.code.sdk.config;

import lombok.Data;

/**
 * 云二维码SDK配置
 *
 * @author system
 */
@Data
public class CloudQrCodeSdkConfig {

    /**
     * 应用ID (client_id)
     */
    private String appId;

    /**
     * 应用密钥 (client_secret)
     */
    private String appSecret;

    /**
     * 服务地址
     * 例如: https://api.example.com
     */
    private String serverUrl;

    /**
     * 连接超时时间(毫秒)，默认5秒
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间(毫秒)，默认30秒
     */
    private int readTimeout = 30000;

    /**
     * 租户ID（可选）
     */
    private String tenantId;

    public CloudQrCodeSdkConfig() {
    }

    public CloudQrCodeSdkConfig(String appId, String appSecret, String serverUrl) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.serverUrl = serverUrl;
    }

    public CloudQrCodeSdkConfig(String appId, String appSecret, String serverUrl, String tenantId) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.serverUrl = serverUrl;
        this.tenantId = tenantId;
    }
}
