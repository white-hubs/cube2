package com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/15 17:03
 * @Description:
 */
@Data
public class VideoAccessibleSnapCaptureVO {

    /**
     * 文件类型
     */
    private String type;

    /**
     * 录像文件地址
     */
    private String captureUrl;
}
