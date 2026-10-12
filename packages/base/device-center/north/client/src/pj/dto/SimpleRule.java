package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

/**
 * 简单规则事件
 * <p>
 * 基于单个设备属性条件判断
 *
 * @author fl
 * @date 2022/10/10
 */
@Data
public class SimpleRule implements Rule {

    /**
     * 属性编码
     */
    private String propCode;

    /**
     * 运算符
     * 如: GT(大于)、LT(小于)、EQ(等于)等
     */
    private String op;

    /**
     * 操作数列表
     * 用于比较的值
     */
    private List<String> oprand;

    /**
     * 延迟时间(毫秒)
     * 规则满足后延迟多久触发事件
     */
    private int delayTime;

    /**
     * 设备ID
     * 针对特定设备的规则
     */
    private String deviceId;

    private TriggerType triggerType = TriggerType.SIMPLE;
}
