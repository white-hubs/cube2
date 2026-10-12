package com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业微信认证返回参数
 *
 * @author lanchun
 * @date 2024/11/18 16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeComAccessTokenResponseDTO extends WeComBaseResponse {

    /**
     * 认证成功令牌
     */
    private String access_token;

    /**
     * 过期时间(单位秒)
     */
    private Integer expires_in;
}
