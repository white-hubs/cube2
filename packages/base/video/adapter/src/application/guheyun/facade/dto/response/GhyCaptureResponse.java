package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/21 15:00
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyCaptureResponse {

    /**
     * 预留资源URL, 提供资源下载URL
     */
    private String resourceUrl;

    /**
     * 预留资源路径, 提供资源路径，具体资源利用方式依赖于实现
     */
    private String resourcePath;

    /**
     * 任务状态: 0-已接收,1-处理中,2-成功,3-失败, 提供正规的异步请求响应模式时提供
     */
    private Integer status;

    /**
     * 请求ID, 提供正规的异步请求响应模式时提供
     */
    private String requestId;
}
