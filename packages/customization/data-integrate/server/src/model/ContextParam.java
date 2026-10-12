package com.guhecloud.cube.base.dataintegrate.server.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContextParam {
    //市场名称
    private String marketName;
    //市场方租户号
    private Integer marketTenant;
    private Integer tenant;
    //请求地址
    private String url;
    //登录账号
    private String account;
    //登录密码
    private String password;
    //token
    private String token;
    //开启状态
    private Boolean status;
    /**
     * 最后更新时间
     */
    private LocalDateTime lastTime;

    public String getFormatUrl(String methodPath) {
        String tempUrl = url.endsWith("/") ? url + "api" : url + "/api";
        return methodPath.startsWith("/") ? tempUrl + methodPath : tempUrl + "/" + methodPath;
    }
}
