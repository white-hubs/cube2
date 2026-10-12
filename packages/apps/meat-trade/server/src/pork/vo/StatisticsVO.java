package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

import java.util.List;

/**
 * 销售统计信息
 */
@Data
public class StatisticsVO {

    /**
     * 当前页统计
     */
    private StatisticsPageVO currentStatistics;

    /**
     * 总计
     */
    private StatisticsPageVO totalStatistics;

    /**
     * 总数
     */
    private int total;

    /**
     * 销售统计信息集合
     */
    private List<StatisticsPageVO> records;

}
