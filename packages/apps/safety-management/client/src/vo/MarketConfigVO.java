package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 10:33
 * @Description:
 */
@Data
public class MarketConfigVO {

    //
    private Long id;
    //市场名称
    private String marketName;
    //市场编号
    private String marketCode;
    //市场方租户号
    private Integer marketTenant;
    //请求地址
    private String url;
    //登录账号
    private String account;
    //登录密码
    private String password;
    /**
     * 是否开启
     */
    private Boolean status;
}
