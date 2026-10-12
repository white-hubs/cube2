package com.guhecloud.subsystem.requester.sdk.vo.video;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/15 15:33
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
