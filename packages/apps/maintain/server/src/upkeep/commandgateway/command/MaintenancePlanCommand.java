package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaintenancePlanCommand {
    /**
     * 维保计划名称
     */
    private String maintenancePlanName;
    /**
     * 责任id
     */
    private String responsibilityPersonId;
    //部门ids
    private List<String> receiveDeptIds;
    //岗位ids
    private List<String> receivePostIds;
    /**
     * 责任名称
     */
    private String responsibilityPersonName;
    /**
     * 责任人类型（部门dep/人员per）
     */
    private String responsibilityType;
    //人员
    private List<String> receiveIds;
    /**
     * 维保周期
     */
    private Integer maintenanceCycle;

    /**
     * 维保周期单位
     */
    private String timeUnit;

    /**
     * 发布时间点
     */
    private Integer maintenanceTimePoint;

    /**
     * 状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划失效时间
     */
    private LocalDateTime planEndTime;

    /**
     * 描述
     */
    private String remark;

    //维保对象
    private List<MaintenanceObjectCommand> maintenanceObjectCommands;
    /**
     * 生成方式
     */
    private String way;
    /**
     * 规则触发
     */
    private List<MaintenancePlanRuleCommand> ruleDetails;
}
