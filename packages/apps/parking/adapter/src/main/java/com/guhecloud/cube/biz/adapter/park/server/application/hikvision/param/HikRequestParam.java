package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param;

import lombok.Data;

/**
 * @author wql
 * @describe 海康接口请求配置
 * @date 2024/3/6 17:38
 */
@Data
public class HikRequestParam {
    /**
     * 平台的ip端口
     */
    private String host;
    /**
     * 密钥appkey
     */
    private String appKey;
    /**
     * 密钥appSecret
     */
    private String appSecret;
}
