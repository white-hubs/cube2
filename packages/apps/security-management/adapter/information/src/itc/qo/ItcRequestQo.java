package com.guhecloud.cube.srv.gen.adapter.information.server.itc.qo;

import lombok.Data;

/**
 * @author xiaofeng
 * @describe itc信息发布接口请求配置
 * @date 2024/10/12 10:01
 * 例:
 * {
 * "httpFlag":"http",
 * "url":"http://127.0.0.1:8080",
 * "username":"username",
 * "password":"password"
 * }
 */
@Data
public class ItcRequestQo {
    /**
     * https/http
     */
    private String httpFlag;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * url
     */
    private String url;
}
