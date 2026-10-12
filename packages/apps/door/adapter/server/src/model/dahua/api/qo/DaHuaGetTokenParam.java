package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.qo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取大华token的入参
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaHuaGetTokenParam {
    /**
     * 授权类型，固定值：password
     * string(0,128)
     */
    @JSONField(name = "grant_type")
    private String grantType;
    /**
     * 用户名
     * string(0,50)
     */
    @JSONField(name = "username")
    private String username;
    /**
     * 平台密码，需RSA加密密码传输,RSA公钥调获取公钥接口
     * string(0,255)
     */
    @JSONField(name = "password")
    private String password;
    /**
     * 客户端id，自定义，支持数字、字母
     * string(0,128)
     */
    @JSONField(name = "client_id")
    private String clientId;
    /**
     * 凭证密钥，访问凭证client_id与client_secret必须和在ICC平台上申请的client_id与client_secret保持一致
     * string(0,128)
     */
    @JSONField(name = "client_secret")
    private String clientSecret;
    /**
     * RSA公钥，调获取公钥接口
     * string(0,512)
     */
    @JSONField(name = "public_key")
    private String publicKey;
    /**
     * 动态验证码
     * string(0,8)
     */
    @JSONField(name = "code")
    private String code;
    /**
     * 是否启用动态验证码功能 0：关闭 1：开启 默认0；5.0.9+支持；
     * 若想开启动态验证码功能，在保证[系统配置->账号安全设置->登录验证码]开关开启的前提下，接口参数需传1，否则开启不生效
     * int(1)
     */
    @JSONField(name = "verifyCodeFlag")
    private Integer verifyCodeFlag;

    /**
     * url:http+ip+端口
     * 例子：http://192.168.1.1:8080
     */
    private String url;
}
