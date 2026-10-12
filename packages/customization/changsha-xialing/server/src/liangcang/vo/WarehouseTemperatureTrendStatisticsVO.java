package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureTrendScope;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 仓库粮温趋势统计。 */
@Data
public class WarehouseTemperatureTrendStatisticsVO {

    /** 数据更新时间。 */
    private LocalDateTime updateTime;
    /** 卡片实时温度。 */
    private List<Card> cards;
    /** 趋势曲线。 */
    private Chart chart;

    @Data
    public static class Card {
        /** 统计范围。 */
        private WarehouseTemperatureTrendScope scope;
        /** 卡片名称。 */
        private String name;
        /** 实时温度。 */
        private BigDecimal temperature;
        /** 状态：NORMAL、HIGH、LOW。 */
        private String status;
        /** 超温预警线。 */
        private BigDecimal highAlarmThreshold;
        /** 低温预警线。 */
        private BigDecimal lowAlarmThreshold;
    }

    @Data
    public static class Chart {
        /** 当前曲线范围。 */
        private WarehouseTemperatureTrendScope scope;
        /** 当前范围超温预警线。 */
        private BigDecimal warningLine;
        /** 最近 5 天实测最高温曲线。 */
        private List<Point> actualSeries;
        /** 预测曲线，本阶段温度固定为 0。 */
        private List<Point> forecastSeries;
    }

    @Data
    public static class Point {
        /** 日期。 */
        private LocalDate date;
        /** 温度点时间。 */
        private LocalDateTime time;
        /** 温度值。 */
        private BigDecimal temperature;
    }
}
