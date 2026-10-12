package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-07  09:26
 * @Description:
 * @Version: 1.0
 */
@Data
public class FaceRecognitionConfigDTO {
    /**
     * 识别成功率
     */
    private Float confidence;
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;

}
