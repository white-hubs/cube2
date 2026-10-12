package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.config;

import lombok.Data;

@Data
public class DifyChatConfig {

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
