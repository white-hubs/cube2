package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 巡检对象分析
 * @date 2023/4/25 20:21
 */
@Data
public class InspectionObjAnalysisVO {
    /**
     * 总巡检次数
     */
    private Long totalInspectionNum;
    /**
     * 总巡检次数环比
     */
    private String totalInspectionNumRatio;
    /**
     * 巡检次数最多对象
     */
    private String maxNumObj;
    /**
     * 巡检次数最多对象巡检次数
     */
    private Long maxNum;
    /**
     * 巡检次数最多对象巡检次数环比
     */
    private String maxNumRatio;
    /**
     * 巡检次数最少对象
     */
    private String minNumObj;
    /**
     * 巡检次数最少对象巡检次数
     */
    private Long minNum;
    /**
     * 巡检次数最少对象巡检次数环比
     */
    private String minNumRatio;

}
