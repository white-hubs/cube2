package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 公众号授权失败返回参数信息
 *
 * @author lanchun
 * @date 2025/1/2 10:25
 */
@Data
public class PublicServiceAccessTokenErrorResponse {

    /**
     * 错误码
     */
    @JsonProperty(value = "errcode")
    private String errCode;

    /**
     * 错误描述
     */
    @JsonProperty(value = "errmsg")
    private Integer errMsg;
}
