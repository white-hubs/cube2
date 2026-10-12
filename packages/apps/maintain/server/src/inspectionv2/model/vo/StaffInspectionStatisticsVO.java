package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 人员巡检统计
 * @date 2023/4/17 18:13
 */
@Data
public class StaffInspectionStatisticsVO {
    /**
     * 人员名称
     */
    private String staffName;
    private Long staffId;
    /**
     * 所属部门
     */
    private String depName;
    private Long depId;
    /**
     * 部门任务总数
     */
    private Long depTaskNum;
    /**
     * 个人任务总数
     */
    private Long personageTaskNum;
    /**
     * 完成任务数
     */
    private Long completeTaskNum;
    /**
     * 过期未执行任务数
     */
    private Long expireTaskNum;
    /**
     * 任务占比
     */
    private String taskProportion;
    /**
     * 任务完成率
     */
    private String taskCompleteRatio;
    /**
     * 稽查平均匹配率
     */
    private String checkMatchingRatio;
    /**
     * 部门巡检次数
     */
    private Long depInspectionNum;
    /**
     * 个人巡检次数
     */
    private Long personageInspectionNum;
    /**
     * 次数占比
     */
    private String personageNumRatio;
    /**
     * 巡检耗时
     */
    private Long inspectionElapsedTime;
    private String inspectionElapsedTimeStr;
    /**
     * 异常隐患数
     */
    private Integer abnormalHiddenDangerNum;
    /**
     * 已办隐患数
     */
    private Integer haveDoneHiddenDangerNum;
    /**
     * 隐患处理率
     */
    private String hiddenDangerDisposeRatio;
    /**
     * 隐患有效占比
     */
    private String hiddenDangerValidRatio;

}
