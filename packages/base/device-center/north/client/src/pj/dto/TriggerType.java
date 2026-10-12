package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

/**
 * @author fl
 * @date 2022/11/15
 **/
public enum TriggerType {
    /**
     * 常规事件，基于单个设备属性条件判断
     */
    SIMPLE,
    /**
     * 针对设备原生事件,需要设定阈值的情况
     */
    SETTING,
    /**
     * 暂未实现
     */
    COMBO,
    /**
     * 设备自身事件
     */
    NATURE
}
