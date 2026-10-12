package com.guhecloud.cube.base.device.center.core.rule.entity;

import com.guhecloud.cube.base.device.center.core.rule.enumeration.ConditionEnum;
import com.guhecloud.cube.base.device.center.core.rule.tag.enumeration.DeviceConditionFieldEnum;
import lombok.Data;

@Data
public class RuleField {

    /**
     * 字段名
     */
    private DeviceConditionFieldEnum field;

    /**
     * 字段条件
     */
    private ConditionEnum operator;

    /**
     * 字段值
     */
    private Object value;
}
