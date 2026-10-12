package com.guhecloud.cube.srv.gen.adapter.information.server.das.qo;

import lombok.Data;

/**
 * @author xiaofeng
 * @describe 达实物联网平台 信息发布接口请求配置
 * @date 2024/11/25 10:01
 * 例:
 * {
 * "httpFlag":"http",
 * "url":"http://127.0.0.1:8080",
 * "username":"username",
 * "password":"password"
 * }
 */
@Data
public class DasRequestQo {
    /**
     * 路径：http://{ip}:{port}
     */
    private String url;
    /**
     * token
     */
    private String token;
}
