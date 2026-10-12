package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 任务耗时统计
 * @date 2023/4/25 20:07
 */
@Data
public class TaskTimeAnalysisVO {
    /**
     * 巡检共计耗时
     */
    private Double totalElapsedTime;
    /**
     * 巡检共计耗时环比
     */
    private String totalElapsedTimeRatio;
    /**
     * 巡检任务类型耗时统计
     */
    private List<InspectionTaskTimeVO> taskTypeTime;
}
