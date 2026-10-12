package com.guhecloud.cube.biz.ouaa.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 微信响应信息
 *
 * @author Woody
 * @date 2021/7/25
 */
@Data
public abstract class WechatResponseDTO {
    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Session extends WechatResponseDTO {
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
    }

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class AccessToken extends WechatResponseDTO {
        /**
         * 授权token
         */
        @JsonProperty("access_token")
        private String accessToken;

        /**
         * 过期时间
         */
        @JsonProperty("expires_in")
        private Integer expiresIn;
    }

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class JsTicket extends WechatResponseDTO {
        /**
         * 授权token
         */
        @JsonProperty("ticket")
        private String ticket;

        /**
         * 过期时间
         */
        @JsonProperty("expires_in")
        private Integer expiresIn;
    }
}
