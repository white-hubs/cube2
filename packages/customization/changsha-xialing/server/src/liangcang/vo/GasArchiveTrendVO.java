package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 气体档案趋势。 */
@Data
public class GasArchiveTrendVO {

    private LocalDate date;

    private List<Item> items;

    @Data
    public static class Item {

        private LocalDateTime sampleTime;

        private BigDecimal co2;

        private BigDecimal o2;

        private BigDecimal ph3;

        private Boolean co2Alarm;

        private Boolean o2Alarm;

        private Boolean ph3Alarm;
    }
}
