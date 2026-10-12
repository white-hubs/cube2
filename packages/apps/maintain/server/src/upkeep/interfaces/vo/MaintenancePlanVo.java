package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanRuleCommand;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaintenancePlanVo {
    /**
     * id
     */
    private Long id;
    /**
     * 维保计划名称
     */
    private String maintenancePlanName;
    /**
     * 责任id
     */
    private String responsibilityPersonId;
    /**
     * 责任人名称
     */
    private String responsibilityPersonName;
    /**
     * 岗位名称
     */
    private String responsibilityPositionName;
    /**
     * 部门名称
     */
    private String responsibilityOrganizationName;
    /**
     * 责任类型
     */
    private String responsibilityType;
    /**
     * 维保周期
     */
    private Integer maintenanceCycle;
    /**
     * 状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime planStartTime;
    /**
     * 部门id
     */
    private String receiveDeptId;

    /**
     * 岗位id
     */
    private String receivePostId;
    /**
     * 计划失效时间
     */
    private LocalDateTime planEndTime;
    /**
     * 任务数
     */
    private Integer taskNum;
    /**
     * 维保周期单位
     */
    private String timeUnit;

    /**
     * 发布时间点
     */
    private Integer maintenanceTimePoint;

    /**
     * 描述
     */
    private String remark;

    //人员
    private List<String> receiveIds;
    //部门ids
    private List<String> receiveDeptIds;
    //岗位ids
    private List<String> receivePostIds;

    //计划对象
    List<MaintenanceObjectVo> maintenaceObjectsList;
    private String way;
    private List<MaintenancePlanRuleCommand> ruleDetails;

    private Integer sort;
}
