package com.guhecloud.cube.base.device.center.metadata.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备标签规则变更消息
 *
 * @author lanchun
 * @date 2025/4/10 13:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTagRuleChange {

    public static final String TOPIC = "DEVICE_TAG_RULE_CHANGE";

    /**
     * 标签编号
     */
    private String tagCode;
}
