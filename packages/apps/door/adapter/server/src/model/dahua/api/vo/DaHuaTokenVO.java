package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaHuaTokenVO {

    /**
     * 保活id
     */
    @JSONField(name = "magicId")
    private String magicId;
    /**
     * 用户名
     */
    @JSONField(name = "user_name")
    private String userName;
    /**
     * 固定[*]
     */
    private String scope;
    /**
     * 鉴权token有效期,单位秒，默认2h
     */
    @JSONField(name = "expires_in")
    private Long expiresIn;
    /**
     * 鉴权token
     * 请求业务接口时设置请求头字段Authorization=token_type + 空格 + access_token，示例：bearer 1：704dc1bc-b05e-4ed5-a889-0261584919f6
     */
    @JSONField(name = "access_token")
    private String accessToken;
    /**
     * 刷新token
     * 刷新token，有效期1天;一般需要结合刷新认证信息接口获取access_token,避免再次输入密码获取
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;
    /**
     * 标识符类型，固定：bearer
     */
    @JSONField(name = "token_type")
    private String tokenType;
    /**
     * 用户id
     */
    @JSONField(name = "userId")
    private String userId;
    /**
     * 来源终端标识
     */
    @JSONField(name = "client_id")
    private String clientId;

}
