package com.guhecloud.cube.base.videogw.server.facade.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频截图抓拍返回参数
 *
 * @author lanchun
 * @date 2023/11/9 15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoSnapCaptureResponse {

    /**
     * 预览的图片地址
     */
    private String previewUrl;
    /**
     * 外网地址
     */
    private String previewUrlOut;
    /**
     * 如果响应为异步模式, 查询的请求存根(暂不实现)
     */
    private String requestId;
}
