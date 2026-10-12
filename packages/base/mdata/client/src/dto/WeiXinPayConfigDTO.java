package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 微信支付配置返回参数
 *
 * @author lanchun
 * @date 2021/12/9 11:31
 */
@Data
public class WeiXinPayConfigDTO {

    private Long id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密码
     */
    private String appSecret;

    /**
     * 直连商户号
     */
    private String mchId;

    /**
     * 支付回调地址
     */
    private String notifyUrl;

    /**
     * API v3密钥
     */
    private String apiV3Key;

    /**
     * 商户证书序列号
     */
    private String mchSerialNo;
}
