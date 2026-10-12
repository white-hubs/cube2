package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

import java.util.List;

@Data
public class MaintenancePlanRuleCommand {
    private Long id;
    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 条件类型(事件或者自定条件)
     */
    private String conditionType;

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
    private String logicalOp;

    /**
     * 预案条件表达式列表
     */
    private List<MaintenanceRuleConditionExpCommand> emergencyRuleConditionExps;
}
