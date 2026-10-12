package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 巡检次数分析
 * @date 2023/4/25 19:43
 */
@Data
public class InspectionFrequencyAnalysisVO {
    /**
     * 巡检任务总数
     */
    private Long taskTotalNum;
    /**
     * 覆盖对象数量
     */
    private Long objNum;
    /**
     * 对象覆盖率
     */
    private String objCoverageRate;
    /**
     * 对象覆盖率环比
     */
    private String objCoverageRateRatio;
    /**
     * 总巡检次数
     */
    private Long inspectionTotalNum;
    /**
     * 总巡检次数环比
     */
    private String inspectionTotalNumRatio;
    /**
     * 巡检次数趋势
     */
    List<TrendOfInspectionTimesVO> trendOfInspectionTimesVOS;
}
