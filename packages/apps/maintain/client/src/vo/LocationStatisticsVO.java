package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 巡更点统计
 * @date 2023/5/30 14:35
 */
@Data
public class LocationStatisticsVO {
    /**
     * 巡更点名称
     */
    private String locationName;
    /**
     * 所属区域
     */
    private String areaName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 应巡次数
     */
    private Integer totalNum;
    /**
     * 实巡
     */
    private Integer realNum;
    /**
     * 漏巡
     */
    private Integer omitNum;
    /**
     * 巡更率
     */
    private String patrolRatio;
    /**
     * 异常次数
     */
    private Integer abnormalNum;
    /**
     * 隐患数
     */
    private Integer hiddenDangerNum;
    /**
     * 隐患处理数
     */
    private Integer hiddenDangerDisposeNum;
}
