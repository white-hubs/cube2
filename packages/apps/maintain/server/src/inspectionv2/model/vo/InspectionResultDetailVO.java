package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 任务明细记录
 * @date 2023/4/17 16:33
 */
@Data
public class InspectionResultDetailVO {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 巡检类型
     */
    private String inspectionType;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 责任人/部门
     */
    private String dutyOfficer;
    /**
     * 接收人
     */
    private String receiveIds;
    /**
     * 接收类型1＝个人，2＝部门
     */
    private String receiveType;
    /**
     * 接收部门
     */
    private String receiveDeptIds;
    /**
     * 接收岗位
     */
    private String receivePostIds;
    /**
     * 接收角色
     */
    private String receiveRoleIds;
    /**
     * 租户
     */
    private Integer tenant;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 任务时限
     */
    private Long taskTimeLimit;
    private String taskTimeLimitStr;
    /**
     * 巡检对象数
     */
    private Long objNum;
    /**
     * 进度
     */
    private String progress;
    /**
     * 状态
     */
    private String state;
    /**
     * 稽查任务关联任务id
     */
    private Long checkTaskId;

}
