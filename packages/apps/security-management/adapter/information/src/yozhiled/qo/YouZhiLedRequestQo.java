package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.qo;

import lombok.Data;

/**
 * @author xiaofeng
 * @describe 优质光电 信息发布请求配置
 * @date 2024/07/30 10:38
 * 例:
 * {
 * "host":"127.0.0.1:6101",
 * "token":"uPS/X8DEYjtE9wXjU7PKyh9uW7mpJDqZordoKIECZ/U=",
 * "service":"terminal",
 * "httpFlag":"http",
 * "interFace":"/API.aspx"
 * }
 */
@Data
public class YouZhiLedRequestQo {
    /**
     * 平台的ip+端口
     */
    private String host;
    /**
     * 请求令牌
     */
    private String token;
    /**
     * 是否HTTP
     */
    private String httpFlag;
    /**
     * 请求接口路径
     */
    private String interFace;
}
