package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** 储量档案变化曲线。 */
@Data
public class WarehouseStorageArchiveTrendVO {

    private String unit;

    private BigDecimal warningValue;

    private Granularity granularity;

    private List<Item> items;

    public enum Granularity {
        HALF_HOUR,
        DAY,
        MONTH
    }

    @Data
    public static class Item {
        private String time;
        private BigDecimal storageValue;
        private BigDecimal warningValue;
        private Boolean alarm;
    }
}
