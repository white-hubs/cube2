package com.guhecloud.cube.biz.adapter.park.server.vo;

import lombok.Data;

/**
 * 大华登录配置信息
 */
@Data
public class DaHuaLoginInfoVO {
    private String host;
    private String port;
    private Boolean isHttp;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
}
