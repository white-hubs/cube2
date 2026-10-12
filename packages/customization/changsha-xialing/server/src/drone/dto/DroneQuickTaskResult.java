package com.guhecloud.cube.customization.xialing.server.drone.dto;

import lombok.Data;

/**
 * 霞凝快速任务启动结果。
 */
@Data
public class DroneQuickTaskResult {
    /** 是否调用成功。 */
    private Boolean success;
    /** 三方原始响应。 */
    private String rawResponse;
    /** 失败原因。 */
    private String errorMessage;
    /** 机库编号。 */
    private String nestSn;
    /** 机库名称。 */
    private String nestName;
    /** 航迹文件类型。 */
    private String trackFileType;
    /** 航迹ID。 */
    private String trackId;
    /** 航迹URL。 */
    private String trackUrl;
}
