package com.guhecloud.cube.biz.ouaa.server.third.wechat.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信公众号网页授权访问参数
 *
 * @author lanchun
 * @date 2025/1/15 14:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatPublicServiceSnsAccessTokenResponse {

    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    /**
     * 用户刷新access_token
     */
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    @JsonProperty(value = "openid")
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    @JsonProperty(value = "scope")
    private String scope;

    /**
     * 是否为快照页模式虚拟账号，只有当用户是快照页模式虚拟账号时返回，值为1
     */
    @JsonProperty(value = "is_snapshotuser")
    private Integer isSnapshotUser;

    /**
     * 用户统一标识（针对一个微信开放平台账号下的应用，同一用户的 unionid 是唯一的），只有当scope为"snsapi_userinfo"时返回
     */
    @JsonProperty(value = "unionid")
    private String unionId;
}
