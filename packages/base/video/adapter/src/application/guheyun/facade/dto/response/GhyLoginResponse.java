package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response;

import lombok.Data;

@Data
public class GhyLoginResponse {

    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 用户主键
     */
    private Long id;

    /**
     * 启用状态
     */
    private Boolean enabled;

}
