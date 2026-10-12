package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.param;

import lombok.Data;

/**
 * @author xiaofeng
 * @describe 海康接口请求配置
 */
@Data
public class HikSpccRequestParam {
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
