package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云黑白灰名单接口配置（从 nacos/Redis 适配器 manufacturerJson 解析）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBlacklistConfig {

    /**
     * 应用唯一标识 app_id
     */
    private String appId;

    /**
     * RSA 私钥 Base64，用于签名
     */
    private String privateKey;

    /**
     * 开放平台 base 地址
     */
    private String baseUrl;

    /**
     * 项目编号 projectCode
     */
    private String projectCode;

}
