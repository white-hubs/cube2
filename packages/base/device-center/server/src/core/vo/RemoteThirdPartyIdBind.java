package com.guhecloud.cube.base.device.center.core.vo;

import lombok.Data;

/**
 * 设备的第三方id绑定事件
 */
@Data
public class RemoteThirdPartyIdBind {
    private String thirdPartyId;
    private Long deviceId;
    /**
     * 事件来源<br/>
     * 2、 IOT平台<br/>
     * 3、 业务平台<br/>
     */
    private int eventSource;
}