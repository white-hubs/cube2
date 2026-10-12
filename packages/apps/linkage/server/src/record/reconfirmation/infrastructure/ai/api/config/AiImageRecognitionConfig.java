package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.config;

import lombok.Data;

@Data
public class AiImageRecognitionConfig {

    /**
     * 厂家编号
     */
    private String manufacturer;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 授权信息
     */
    private String token;

    /**
     * 模型
     */
    private String model;
}
