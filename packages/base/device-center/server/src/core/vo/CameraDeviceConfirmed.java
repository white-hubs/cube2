package com.guhecloud.cube.base.device.center.core.vo;

import lombok.Data;

@Data
public class CameraDeviceConfirmed {
    private Long deviceId;
    /**
     * 事件来源<br/>
     * 1、 接入层<br/>
     * 2、 IOT平台<br/>
     * 3、 业务平台<br/>
     */
    private int eventSource;
}
