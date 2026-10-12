package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import lombok.Data;

@Data
public class MockPropReport {
    /**
     * 租户ID
     */
    private Long tenantId;

    private String parentDeviceId;
    private Long productId;
    /**
     * 接入层补齐ID与number
     */
    private Long deviceId;
    private String deviceNumber;
    private String propCode;
    private Object propValue;
    private long ts;
}
