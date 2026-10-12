package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 对象巡检异常项分析
 * @date 2023/4/25 20:30
 */
@Data
public class InspectionObjAbnormalAnalysisVO {
    /**
     * 对象异常项总数
     */
    private Long abnormalObjNum;
    /**
     * 对象异常项总数环比
     */
    private String abnormalObjNumRatio;
    /**
     * 巡检异常项最多对象
     */
    private String maxAbnormalObj;
    /**
     * 巡检异常项最多对象异常项总数
     */
    private Long maxAbnormalObjNum;
    /**
     * 巡检异常项最多对象异常项总数环比
     */
    private String maxAbnormalObjNumRatio;
    /**
     * 巡检异常项最少对象
     */
    private String minAbnormalObj;
    /**
     * 巡检异常项最少对象异常项总数
     */
    private Long minAbnormalObjNum;
    /**
     * 巡检异常项最少对象异常项总数环比
     */
    private String minAbnormalObjNumRatio;
}
