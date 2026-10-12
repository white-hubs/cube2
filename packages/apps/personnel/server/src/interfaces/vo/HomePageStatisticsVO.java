package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/30 15:59
 * @Description:
 */

@Data
public class HomePageStatisticsVO {

    /**
     * 总人数
     */
    private Long total;

    /**
     * 环比
     */
    private Float chinRatio;

    /**
     * 正式人数
     */
    private Long formalCount;

    /**
     * 试用人数
     */
    private Long trialCount;

    /**
     * 入职人数
     */
    private Long entryCount;

    /**
     * 转正人数
     */
    private Long formalizedCount;

    /**
     * 离职人数
     */
    private Long leaveCount;
}
