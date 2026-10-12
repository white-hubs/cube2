package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 稽查结果分析
 * @date 2023/4/25 20:16
 */
@Data
public class AnalysisOfAuditResultsVO {
    /**
     * 稽查任务总数
     */
    private Integer totalCheckTaskNum;
    /**
     * 稽查任务总数环比
     */
    private String totalCheckTaskNumRatio;
    /**
     * 稽查结果匹配率top5
     */
    private List<CheckResultMatchVO> top5Match;
    /**
     * 稽查结果匹配率last5
     */
    private List<CheckResultMatchVO> last5Match;
}
