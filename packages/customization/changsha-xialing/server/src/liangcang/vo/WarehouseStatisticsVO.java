package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仓统计总返回体。
 */
@Data
public class WarehouseStatisticsVO {
    /** 各时间桶汇总行。 */
    private List<WarehouseStatisticsRowVO> items;
    /**
     * 日维：同时间段内每次采集一行；非日维为空列表。
     */
    private List<WarehouseStatisticsAcquisitionDetailVO> acquisitionItems;
    /**
     * 日维且指定单仓时：与 acquisitionItems 对应的每次周转简项；否则空。
     */
    private List<AcquisitionTurnoverItemVO> acquisitionTurnoverItems;
    /** 全段平均周转率（基于 items）。 */
    private BigDecimal averageTurnoverRate;
    /** 去年同段全段均周转。 */
    private BigDecimal lastYearSamePeriodAverageTurnoverRate;
    /** 全段平均周转率同比变化率(%) 等，见业务约定。 */
    private BigDecimal totalYearOnYearChangeRate;
    /** 紧邻上一等长区间全段均周转。 */
    private BigDecimal previousPeriodAverageTurnoverRate;
    /** 全段环比变化率(%) 等。 */
    private BigDecimal totalChainRatioChangeRate;
    /** 各时间维桶下平均周转。 */
    private List<TimeDimensionTurnoverVO> timeDimensionTurnovers;
    /** 各桶周转同比趋势(%)。 */
    private List<TurnoverTrendItemVO> timeDimensionTurnoverYearOnYearTrend;
    /** 各桶周转环比趋势(%)。 */
    private List<TurnoverTrendItemVO> timeDimensionTurnoverChainRatioTrend;

    @Data
    public static class TimeDimensionTurnoverVO {
        /** 与 items 中时间维标签一致。 */
        private String timeDimension;
        /** 该时间维下平均周转率。 */
        private BigDecimal averageTurnoverRate;
    }

    @Data
    public static class TurnoverTrendItemVO {
        /** 时间维桶，与 timeDimensionTurnovers 中标签对齐。 */
        private String timeDimension;
        /** 较基期变化率(%)，不可算时为 null。 */
        private BigDecimal changeRate;
    }

    @Data
    public static class AcquisitionTurnoverItemVO {
        /** 日标签，与当次采集自然日一致。 */
        private String timeDimension;
        /** 当次测点周转率。 */
        private BigDecimal turnoverRate;
        /** 采集时间，ISO-8601。 */
        private String acquisitionTime;
    }
}
