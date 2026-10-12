package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/16 14:18
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolFileVO {

    private Long fileId;
    private String extranetFilePath;
    private String intranetFilePath;
}
