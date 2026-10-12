package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  11:35
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum FaceRecognitionTypeEnum {
    KAIHE("开河平台"),
    DUMU("度目"),
    ALIYUN("阿里云");
    private final String remarks;

    FaceRecognitionTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
