package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

/**
 * 阈值设置规则
 * <p>
 * 针对设备原生事件，需要设定阈值的情况
 *
 * @author fl
 * @date 2022/10/10
 */
@Data
public class SettingRule implements Rule {

    /**
     * 针对一个设备做阈值设置
     */
    private String deviceId;

    /**
     * 针对一个产品做阈值设置
     */
    private Long productId;

    /**
     * 阈值设置列表
     */
    private List<Entry> setValues;

    private TriggerType triggerType = TriggerType.SETTING;
}
