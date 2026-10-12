package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import com.alibaba.excel.annotation.ExcelIgnore;
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
    @ExcelIgnore
    private String areaId;
    /**
     * 巡更点id
     */
    @ExcelIgnore
    private Long locationId;
    /**
     * 应巡次数
     */
    private Long totalNum;
    /**
     * 实巡
     */
    private Long realNum;
    /**
     * 漏巡
     */
    private Long omitNum;
    /**
     * 巡更率
     */
    private String patrolRatio;
    /**
     * 异常次数
     */
    private Long abnormalNum;
    /**
     * 隐患数
     */
    private Integer hiddenDangerNum;
    /**
     * 隐患处理数
     */
    private Integer hiddenDangerDisposeNum;
}
