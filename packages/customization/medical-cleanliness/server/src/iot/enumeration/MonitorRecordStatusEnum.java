package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-22  09:53
 * @Description:
 * @Version: 1.0
 */
public enum MonitorRecordStatusEnum {
    NORMAL("正常"),
    ABNORMAL("异常"),
    INVALID("无效"),

    ;
    private String remarks;

    MonitorRecordStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
