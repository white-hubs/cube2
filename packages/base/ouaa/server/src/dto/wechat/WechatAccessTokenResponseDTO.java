package com.guhecloud.cube.biz.ouaa.server.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatAccessTokenResponseDTO {
    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String key;

    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回
     */
    @JsonProperty("unionid")
    private String unionId;
    /**
     * 授权token
     */
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 过期时间
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
