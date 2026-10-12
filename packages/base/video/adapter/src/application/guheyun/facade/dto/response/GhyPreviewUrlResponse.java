package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取播放地址返回参数
 *
 * @author lanchun
 * @date 2024/10/22 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyPreviewUrlResponse {

    /**
     * 主码流
     */
    private String mainStreamUrl;

    /**
     * 辅码流
     */
    private String subStreamUrl;
}
