package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 宇视云平台配置
 *
 * @author lanchun
 * @date 2025/5/29 11:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniViewConfig {

    /**
     * 协议类型
     */
    private String schema;

    /**
     * http+ip+端口
     * string(0,128)
     */
    private String host;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 秘钥
     */
    private String secretKey;

    /**
     * 播放地址
     */
    private String previewUrl;

    /**
     * 抓拍地址
     */
    private String snapCaptureUrl;
}
