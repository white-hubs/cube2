package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.GET_TOKEN;

/**
 * 获取大华token的入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaGetTokenRequest extends AbstractDaHuaRequest {
    /**
     * 授权类型，固定值：password
     * string(0,128)
     */
    @JsonProperty("grant_type")
    private String grantType;
    /**
     * 用户名
     * string(0,50)
     */
    @JsonProperty("username")
    private String username;
    /**
     * 平台密码，需RSA加密密码传输,RSA公钥调获取公钥接口
     * string(0,255)
     */
    @JsonProperty("password")
    private String password;
    /**
     * 客户端id，自定义，支持数字、字母
     * string(0,128)
     */
    @JsonProperty("client_id")
    private String clientId;
    /**
     * 凭证密钥，访问凭证client_id与client_secret必须和在ICC平台上申请的client_id与client_secret保持一致
     * string(0,128)
     */
    @JsonProperty("client_secret")
    private String clientSecret;
    /**
     * RSA公钥，调获取公钥接口
     * string(0,512)
     */
    @JsonProperty("public_key")
    private String publicKey;
    /**
     * 动态验证码
     * string(0,8)
     */
    @JsonProperty("code")
    private String code;
    /**
     * 是否启用动态验证码功能 0：关闭 1：开启 默认0；5.0.9+支持；
     * 若想开启动态验证码功能，在保证[系统配置->账号安全设置->登录验证码]开关开启的前提下，接口参数需传1，否则开启不生效
     * int(1)
     */
    @JsonProperty("verifyCodeFlag")
    private Integer verifyCodeFlag;

    /**
     * url:http+ip+端口
     * 例子：http://192.168.1.1:8080
     */
    private String url;

    @Override
    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public String getRequestUrl() {
        return getRequestUrl(GET_TOKEN).build();
    }
}
