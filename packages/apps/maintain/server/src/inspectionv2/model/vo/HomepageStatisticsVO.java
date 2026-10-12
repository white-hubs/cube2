package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/30 11:05
 * @Description:
 */

@Data
@Builder
public class HomepageStatisticsVO {

    /**
     * 完成率
     */
    private Float completeRatio;

    /**
     * 异常率
     */
    private Float abnormalRatio;

    /**
     * 今年巡检次数
     */
    private StatisticInfo thisYearInspectionCount;

    /**
     * 正常次数
     */

    private StatisticInfo normalCount;

    /**
     * 异常次数
     */
    private StatisticInfo abnormalCount;

    /**
     * 今年巡检任务数
     */
    private StatisticInfo thisYearInspectionTaskCount;

    /**
     * 进行中任务数
     */

    private StatisticInfo ongoingTaskCount;

    /**
     * 过期未完成任务数
     */

    private StatisticInfo expiredTaskCount;

    @Data
    @Builder
    public static class StatisticInfo {
        /**
         * 数量
         */
        private Long num;
        /**
         * 环比
         */
        private Float ratio;
    }
}
