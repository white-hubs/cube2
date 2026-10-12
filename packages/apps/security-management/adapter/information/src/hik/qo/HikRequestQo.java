package com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo;

import lombok.Data;

/**
 * @author wql
 * @describe 海康接口请求配置
 * @date 2024/3/6 17:38
 * 例:
 * {
 * "host":"127.0.0.1:443",
 * "appKey":"24869919",
 * "appSecret":"RtavTHsVqSXarLL6yxth"
 * }
 */
@Data
public class HikRequestQo {
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
