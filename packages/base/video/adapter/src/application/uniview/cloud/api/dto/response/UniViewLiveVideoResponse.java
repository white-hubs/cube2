package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取播放地址返回参数
 *
 * @author lanchun
 * @date 2025/5/29 11:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniViewLiveVideoResponse {

    /**
     * 直播地址
     */
    private String url;

    /**
     * 地址状态描述：（0-推流中 1-停止 2-错误）
     */
    private Integer status;
}
