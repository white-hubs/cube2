package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 公众号授权返回参数信息
 *
 * @author lanchun
 * @date 2025/1/2 10:25
 */
@Data
public class PublicServiceAccessTokenResponse {

    /**
     * 获取到的凭证
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;
}
