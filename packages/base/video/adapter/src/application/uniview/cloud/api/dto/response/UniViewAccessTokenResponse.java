package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response;

import lombok.Data;

/**
 * 获取AccessToken返回参数
 *
 * @author lanchun
 * @date 2025/5/29 11:37
 */
@Data
public class UniViewAccessTokenResponse {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 过期时间，UTC时间戳
     */
    private Long expireTime;
}
