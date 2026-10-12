package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto;

import lombok.Data;

/**
 * @author lanchun
 * @date 2022/6/28 9:50
 */
@Data
public class RuleIotTriggerSnapshot {

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性值
     */
    private String propValue;

    /**
     * 取得的属性的时间戳
     */
    private String propValueTs;

    /**
     * 调试状态下有， 包含当前实际执行的比较规则
     */
    private String condition;
}
