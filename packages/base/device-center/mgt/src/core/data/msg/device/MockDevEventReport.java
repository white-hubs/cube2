package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import lombok.Data;

import java.util.Map;

/**
 * 设备事件
 *
 * @author fl
 * @date 2022/10/21
 **/
@Data
public class MockDevEventReport {
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 事件ID
     */
    private String msgId;
    private Long productId;
    private Long deviceId;
    private String eventCode;
    private String name;//optional
    private long ts;

    private Map<String, Object> relatedProp;

    @Override
    public String toString() {
        return "DeviceEventMessage{" +
                "instanceId='" + msgId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", name='" + name + '\'' +
                ", ts=" + ts +
                ", relatedProp=" + relatedProp +
                '}';
    }
}
