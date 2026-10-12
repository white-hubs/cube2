package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 巡检任务分析
 * @date 2023/4/25 19:36
 */
@Data
public class InspectionTaskAnalysisVO {
    /**
     * 巡检任务总数
     */
    private Long taskTotalNum;
    /**
     * 巡检任务总数环比
     */
    private String taskTotalNumRatio;
    /**
     * 正常完成任务数
     */
    private Long completeTaskNum;
    /**
     * 正常完成任务数环比
     */
    private String completeTaskNumRatio;
    /**
     * 正常完成任务数占比
     */
    private String completeTaskNumProportion;
    /**
     * 结果异常任务数
     */
    private Long abnormalTaskNum;
    /**
     * 结果异常任务数环比
     */
    private String abnormalTaskNumRatio;
    /**
     * 结果异常任务数占比
     */
    private String abnormalTaskNumProportion;
    /**
     * 过期未执行任务数
     */
    private Long expireTaskNum;
    /**
     * 过期未执行任务数环比
     */
    private String expireTaskNumRatio;
    /**
     * 过期未执行任务占比
     */
    private String expireTaskNumProportion;
    /**
     * 期间巡检任务数
     */
    private Long periodTaskNum;
    /**
     * 巡检任务数
     */
    private Long taskNum;
    /**
     * 期间巡检任务占比
     */
    private String periodTaskNumProportion;

}
