package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:36
 * @Description:
 * @Version: 1.0
 */
@Data
public class MonthlyReportVO {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 月报月份
     */
    private String monthly;
    /**
     * 月报时间
     */
    private LocalDateTime monthDate;
    /**
     * 填报状态
     */
    private String status;

    /**
     * 填报状态名
     */
    private String statusName;

    /**
     * 填报数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reportData;

    /**
     * 上报人
     */
    private Long reportUserId;
    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    /**
     * 文件地址
     */
    private String reportUrl;

    /**
     * 文件id
     */
    private Long reportId;

    /**
     * 文件名称
     */
    private String reportName;

    /**
     * 未完成填报的市场
     */
    private String nonCompletedMarkets;

    /**
     * 统计信息
     */
    private ReportStatisticsVO reportStatistics;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportStatisticsVO {

        /**
         * 汇总市场数
         */
        private Integer marketCount;

        /**
         * 已上报市场
         */
        private Integer windUpCount;

        /**
         * 未上报市场
         */
        private Integer nonWindUpCount;

        public Integer getNonWindUpCount() {
            if (0 == windUpCount) {
                return this.marketCount;
            }
            return marketCount - windUpCount;
        }
    }
}
