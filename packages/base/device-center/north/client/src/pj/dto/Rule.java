package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

/**
 * 事件规则标记接口
 *
 * @author fl
 * @date 2022/11/15
 */
public interface Rule {
    /**
     * 获取触发类型
     *
     * @return 触发类型
     */
    TriggerType getTriggerType();
}
