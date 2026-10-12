package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectPlanVO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 计划级别(紧急、普通)
     */
    private InspectLevelEnum level;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 1开启 0关闭
     */
    private Boolean enable;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private InspectPlanExecStatusEnum status;

    /**
     * json字符串
     */
    private String limitTimeJson;

    /**
     * 循环类型 0=单次 1=循环
     */
    private Boolean cycle;

    /**
     * 表达式ID
     */
    private Long cronId;

    /**
     * 表达式名
     */
    private String cronName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 接收类型，1＝个人，2＝部门
     */
    private String receiveType;

    /**
     * 接收部门
     */
    private String receiveDeptIds;

    private String receiveDeptNames;

    /**
     * 接收岗位
     */
    private String receivePostIds;

    private String receivePostNames;

    /**
     * 接收角色
     */
    private String receiveRoleIds;

    private String receiveRoleNames;

    /**
     * 接收对象ID
     */
    private String receiveIds;

    private String receiveNames;

    /**
     * 是否拆分
     */
    private boolean isSplit;

    /**
     * 是否检查
     */
    private boolean isCheck;

    /**
     * 稽查部门
     */
    private String checkDeptIds;

    private String checkDeptNames;

    /**
     * 稽查岗位
     */
    private String checkPostIds;

    private String checkPostNames;

    /**
     * 稽查角色
     */
    private String checkRoleIds;

    private String checkRoleNames;

    /**
     * 稽查人/部门id和名称
     */
    private String checkUserIds;

    private String checkUserNames;

    /**
     * 配置巡检目标对象数
     */
    private Integer targetNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人名称
     */
    private String createByName;

    List<InspectPlanTargetReferVO> planTargets;

}
