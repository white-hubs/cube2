package com.guhecloud.cube.base.videogw.client.dto;

import lombok.Data;

/**
 * 视频录像返回参数
 *
 * @author lanchun
 * @date 2023/11/9 15:18
 */
@Data
public class VideoCaptureDTO {

    /**
     * 记录id
     */
    private Long id;

    /**
     * 录像文件地址
     */
    private String captureUrl;

    /**
     * 如果响应为异步模式, 查询的请求存根(暂不实现)
     */
    private String requestId;
}
