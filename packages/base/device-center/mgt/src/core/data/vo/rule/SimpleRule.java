package com.guhecloud.cube.iot.mgt.core.data.vo.rule;

import lombok.Data;

import java.util.List;

/**
 * 常规事件，基于单个设备属性条件判断
 *
 * @author fl
 * @date 2022/10/10
 **/
@Data
public class SimpleRule implements Rule {
    private TriggerType triggerType;
    private String propCode;
    private String op;
    private List<String> oprand;
    private int delayTime;
    private String deviceId;

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.SIMPLE;
    }
}