package com.guhecloud.cube.base.videogw.server.facade.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频录像返回参数
 *
 * @author lanchun
 * @date 2023/11/9 15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoCaptureResponse {

    /**
     * 录像文件地址
     */
    private String captureUrl;

    /**
     * 如果响应为异步模式, 查询的请求存根(暂不实现)
     */
    private String requestId;
}
