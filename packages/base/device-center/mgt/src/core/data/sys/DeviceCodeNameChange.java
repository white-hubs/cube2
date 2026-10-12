package com.guhecloud.cube.iot.mgt.core.data.sys;

import lombok.Data;

@Data
public class DeviceCodeNameChange {
    private Long deviceId;
    private String deviceNumber;
    private String name;
    /**
     * 事件来源<br/>
     * 1、 接入层<br/>
     * 2、 IOT平台<br/>
     * 3、 业务平台<br/>
     */
    private int eventSource;
}
