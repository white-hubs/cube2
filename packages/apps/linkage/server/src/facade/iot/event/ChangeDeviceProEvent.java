package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeDeviceProEvent {

    /**
     * 命令标识（用于执行结果回调）
     */
    private String commandSeq;

    /**
     * 属性名称
     */
    private String deviceId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性值
     */
    private String propValue;
}
