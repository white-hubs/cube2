package com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo;

import lombok.Data;

/**
 * 视频截图抓拍返回参数
 *
 * @author lanchun
 * @date 2023/11/9 15:18
 */
@Data
public class VideoSnapCaptureVO {

    /**
     * 预览的图片地址
     */
    private String previewUrl;

    /**
     * 如果响应为异步模式, 查询的请求存根(暂不实现)
     */
    private String requestId;

    /**
     * 外网地址
     */
    private String previewUrlOut;
}
