package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡检计划类型饼状图
 * @date 2023/4/18 10:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProportionOfPlanTypesVO {
    /**
     * 计划类型
     */
    private String planType;
    /**
     * 巡检计划数量
     */
    private Long planNum;
    /**
     * 计划类型占比
     */
    private String proportion;

}
