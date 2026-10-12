package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/15 15:03
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolSnapCaptureVO {
    /**
     * 记录id
     */
    private Long id;

    /**
     * 预览的图片地址
     */
    private String previewUrl;
}
