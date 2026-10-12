package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.REFRESH_TOKEN;

/**
 * 刷新大华token的入参
 * 请求示例：{
 * "grant_type":"refresh_token",
 * "client_id":"xxx",
 * "client_secret":"xxx",
 * "refresh_token":"xxx"
 * }
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaRefreshTokenRequest extends AbstractDaHuaRequest {
    /**
     * 认证类型，固定值：refresh_token
     * string(0,128)
     */
    @JSONField(name = "grant_type")
    private String grantType;
    /**
     * 客户端id，自定义，支持数字、字母，即认证接口中的client_id与client_secret
     * string(0,128)
     */
    @JSONField(name = "client_id")
    private String clientId;
    /**
     * 客户端secret，，详见申请访问凭证获取
     * string(0,128)
     */
    @JSONField(name = "client_secret")
    private String clientSecret;
    /**
     * refresh_token
     * string(0,2048)
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;

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
        return getRequestUrl(REFRESH_TOKEN).build();
    }
}
