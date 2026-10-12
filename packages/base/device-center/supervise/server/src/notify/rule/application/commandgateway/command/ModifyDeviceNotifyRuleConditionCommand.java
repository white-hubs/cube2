package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.LogicalOpEnum;
import lombok.Data;

import java.util.List;

@Data
public class ModifyDeviceNotifyRuleConditionCommand {

    /**
     * 条件类型(事件或者自定条件)
     */
    private DeviceRuleConditionTypeEnum conditionType;

    /**
     * 设备事件主键
     */
    private String deviceEventId;

    /**
     * 设备事件编号
     */
    private String deviceEventCode;

    /**
     * 设备事件名称
     */
    private String deviceEventName;

    /**
     * 设备事件类型
     */
    private String deviceEventType;

    /**
     * 逻辑运算符（or and）
     */
    private LogicalOpEnum logicalOp;

    /**
     * 通知规则条件表达式列表
     */
    private List<ModifyDeviceNotifyRuleConditionExpCommand> ruleConditionExps;
}
