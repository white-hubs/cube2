package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

@Data
public class GmLoginTokenDTO {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 租户类型
     */
    private String tenantType;
    /**
     * 过期时长
     */
    private Long expiresIn;
    /**
     * 租户编号
     */
    private String tenantCode;
    /**
     * token
     */
    private String accessToken;

    /**
     * 登录及地址
     */
    private String baseUrl;
}
