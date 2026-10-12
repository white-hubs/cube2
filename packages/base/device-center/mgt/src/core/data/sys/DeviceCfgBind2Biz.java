package com.guhecloud.cube.iot.mgt.core.data.sys;

import lombok.Data;

/**
 * 设备的第三方id绑定事件
 */
@Data
public class DeviceCfgBind2Biz {
    private String cfg;
    private Long deviceId;
    /**
     * 事件来源<br/>
     * 1、 接入层<br/>
     * 2、 IOT平台<br/>
     * 3、 业务平台<br/>
     */
    private int eventSource;
}