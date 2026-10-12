package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 对象任务统计分析
 * @date 2023/4/25 20:34
 */
@Data
public class InspectionObjTaskAnalysisVO {
    /**
     * 巡检任务总数
     */
    private Long inspectionTaskTotalNum;
    /**
     * 巡检任务最多对象
     */
    private String maxTaskNumObj;
    /**
     * 巡检任务最多对象的任务总数
     */
    private Long maxTaskNum;
    /**
     * 巡检任务最多对象的任务总数环比
     */
    private String maxTaskNumRatio;
    /**
     * 巡检任务最少对象
     */
    private String minTaskNumObj;
    /**
     * 巡检任务最少对象的任务总数
     */
    private Long minTaskNum;
    /**
     * 巡检任务最少对象的任务总数环比
     */
    private String minTaskNumRatio;
}
