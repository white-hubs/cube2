package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 任务执行分析
 * @date 2023/4/25 19:51
 */
@Data
public class TaskExecutionAnalysisVO {
    /**
     * 任务总数
     */
    private Long totalTaskNum;
    /**
     * 超时完成任务数
     */
    private Long overTimeTaskNum;
    /**
     * 过期未执行任务数
     */
    private Long expiredTaskNum;
    /**
     * 结果异常任务数
     */
    private Integer abnormalTaskNum;
    /**
     * 任务总数环比
     */
    private String totalTaskNumRatio;
}
