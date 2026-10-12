package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备数据共享配置变更事件
 */
@Data
public class DataShareCfgEventChange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件类型: ADD / UPDATE / DELETE
     */
    private String eventType;

    /**
     * 共享规则ID
     */
    private Long ruleId;

    /**
     * 源设备ID（用于定向更新缓存，可选）
     */
    private Long sourceDeviceId;

    /**
     * 时间戳
     */
    private Long timestamp;

    @Override
    public String toString() {
        return "DataShareCfgEventChange{" +
                "eventType='" + eventType + '\'' +
                ", ruleId=" + ruleId +
                ", sourceDeviceId=" + sourceDeviceId +
                ", timestamp=" + timestamp +
                '}';
    }
}
