package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.constants.UniViewApiConstant.UNI_VIEW_ACCESS_TOKEN_GET;

/**
 * 获取accessToken请求参数
 *
 * @author lanchun
 * @date 2025/5/29 11:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UniViewAccessTokenRequest extends AbstractRequest {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 密钥
     */
    private String secretKey;

    @Override
    protected HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public String getRequestUrl() {
        return getRequestUrl(UNI_VIEW_ACCESS_TOKEN_GET).build();
    }
}
