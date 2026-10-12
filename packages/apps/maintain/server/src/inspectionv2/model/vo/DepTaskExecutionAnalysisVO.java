package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 部门巡检任务分析
 * @date 2023/4/25 19:57
 */
@Data
public class DepTaskExecutionAnalysisVO {
    /**
     * 完成任务最多部门
     */
    private String maxTaskNumDep;
    /**
     * 完成任务最多部门完成任务数
     */
    private Long maxTaskNum;
    /**
     * 完成任务最多部门完成任务数环比
     */
    private String maxTaskNumRatio;
    /**
     * 完成率最高部门
     */
    private String maxTaskRatioDep;
    /**
     * 完成率最高部门完成率
     */
    private String maxTaskRatio;
    /**
     * 完成率最高部门完成列表环比
     */
    private String maxTaskRatioRatio;
    /**
     * 完成任务最少部门
     */
    private String minTaskNumDep;
    /**
     * 完成任务最少部门完成任务数
     */
    private Long minTaskNum;
    /**
     * 完成任务最少部门完成任务数环比
     */
    private String minTaskNumRatio;
    /**
     * 完成率最低部门
     */
    private String minTaskRatioDep;
    /**
     * 完成率最低部门完成率
     */
    private String minTaskRatio;
    /**
     * 完成率最低部门完成率环比
     */
    private String minTaskRatioRatio;
    /**
     * 部门任务完成情况
     */
    private List<DepInspectionTaskInfoVO> addDepTask;
}
