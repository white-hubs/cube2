package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import lombok.Data;

/** 平台账号密码登录配置（系统字典 variable）。 */
@Data
public class LiangcangPlatformLoginCredentialDTO {
    private String requestUrl;
    private String url;
    private String authServerUri;
    private String username;
    private String account;
    private String password;
    private String clientId;
    private String clientSecret;
    private Integer tenant;
    private String terminal;
}
