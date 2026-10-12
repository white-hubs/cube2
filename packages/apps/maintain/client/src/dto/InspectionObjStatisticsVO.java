package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 16:46
 * @Description:
 */
@Data
public class InspectionObjStatisticsVO {
    /**
     * 对象名称
     */
    private String objName;
    /**
     * 对象id
     */
    private Long objId;
    /**
     * 对象类型
     */
    private String objType;
    /**
     * 所在区域
     */
    private String area;
    /**
     * 巡检次数
     */
    private Integer inspectionNum;
    /**
     * 巡检任务数
     */
    private Integer taskNum;
    /**
     * 异常隐患数
     */
    private Integer abnormalHiddenDangerNum;
    /**
     * 有效隐患数
     */
    private Integer validHiddenDangerNum;
    /**
     * 已处理隐患数
     */
    private Integer processedHiddenDangerNum;
    /**
     * 未处理隐患数
     */
    private Integer untreatedHiddenDangerNum;
    /**
     * 隐患处理率
     */
    private String hiddenDangerDisposeRatio;
    /**
     * 报修次数
     */
    private Integer repairsNum;
    /**
     * 巡检平均耗时
     */
    private String inspectionAveTime;

}
