package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新token VO类
 * 返回示例{
 * "success": true,
 * "data": {
 * "access_token": "1:41b47980-35a9-406d-a843-d875d672de82",
 * "token_type": "bearer",
 * "refresh_token": "0ef5e3f7-5779-4ad2-94c6-34a972feb6fb",
 * "expires_in": 86399,
 * "scope": "*",
 * "userId": "1",
 * "magicId": "1868a7db-adeb-42de-8064-43b3706ae407"
 * },
 * "code": "0",
 * "errMsg": ""
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaHuaRefreshTokenVO {
    /**
     * 鉴权token
     * 请求业务接口时设置请求头字段Authorization=token_type + 空格 + access_token，示例：bearer 1：704dc1bc-b05e-4ed5-a889-0261584919f6
     */
    @JSONField(name = "access_token")
    private String accessToken;

    /**
     * 刷新token，有效期1天;一般需要结合刷新认证信息接口获取access_token,避免再次输入密码获取
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;

    /**
     * 令牌类型，固定bearer
     */
    @JSONField(name = "token_type")
    private String tokenType;
    /**
     * access_token有效期,单位是秒
     */
    @JSONField(name = "expires_in")
    private Long expiresIn;
    /**
     * 作用域，固定‘*’
     */
    @JSONField(name = "scope")
    private String scope;
    /**
     * 用户id
     */
    @JSONField(name = "userId")
    private String userId;
    /**
     * 保活id
     */
    @JSONField(name = "magicId")
    private String magicId;
}
