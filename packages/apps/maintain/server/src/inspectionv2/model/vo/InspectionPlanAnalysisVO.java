package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 巡检计划分析
 * @date 2023/4/25 19:07
 */
@Data
public class InspectionPlanAnalysisVO {
    /**
     * 巡检计划总数
     */
    private Long patrolSchemeTotal;
    /**
     * 计划类型饼状图
     */
    List<ProportionOfPlanTypesVO> proportionOfPlanTypesVOS;
}
