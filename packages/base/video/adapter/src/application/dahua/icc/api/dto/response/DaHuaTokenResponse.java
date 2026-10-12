package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaHuaTokenResponse {

    /**
     * 保活id
     */
    @JsonProperty("magicId")
    private String magicId;
    /**
     * 用户名
     */
    @JsonProperty("user_name")
    private String userName;
    /**
     * 固定[*]
     */
    private String scope;
    /**
     * 鉴权token有效期,单位秒，默认2h
     */
    @JsonProperty("expires_in")
    private Long expiresIn;
    /**
     * 鉴权token
     * 请求业务接口时设置请求头字段Authorization=token_type + 空格 + access_token，示例：bearer 1：704dc1bc-b05e-4ed5-a889-0261584919f6
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 刷新token
     * 刷新token，有效期1天;一般需要结合刷新认证信息接口获取access_token,避免再次输入密码获取
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
    /**
     * 标识符类型，固定：bearer
     */
    @JsonProperty("token_type")
    private String tokenType;
    /**
     * 用户id
     */
    @JsonProperty("userId")
    private String userId;
    /**
     * 来源终端标识
     */
    @JsonProperty("client_id")
    private String clientId;

}
