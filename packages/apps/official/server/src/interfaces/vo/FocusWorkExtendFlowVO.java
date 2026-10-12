package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项业务表
 */
@Data
public class FocusWorkExtendFlowVO {
    private Long id;

    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名

    private Long applicantDeptId;//部门ID

    private String applicantDeptName;//部门名称

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间

    private String workDesc;//事项描述

    private Long workCategoryId;//事项类型

    private String workCategoryName;

    private String basis;//督办依据

    private String requirement;//工作要求
    /**
     * 牵头领导
     */
    private String leadLeaderUserId;
    private String leadLeaderUserName;
    private String deptId;//部门ID

    private String deptName;//部门名称

    private String firstUserId;//第一责任人

    private String firstUserName;//姓名

    private String secondUserId;//第二责任人

    private String secondUserName;//姓名

    private LocalDateTime completeTime;//完成时间

    private String completeTimeStr;//用于前端展示

    private LocalDateTime updateCompleteTime;//更新后完成时间

    private String updateCompleteTimeStr;

    private Float percentage;//进度百分比

    private String status;//事项当前状态

    private String timeLimitDesc;//超期描述
    //流程实例ID
    private String processInstanceId;
    //流程名称
    private String processInstanceName;
    //流程编号
    private String processInstanceCode;
    //流程状态
    private String processInstanceStatus;
    //流程节点
    private String processInstanceNode;

    private String focusWorkStarterId;//任务下发人ID

    private String focusWorkStarter;//任务下发人名称
    /**
     * 落实情况
     */
    private String practicableDetails;
}