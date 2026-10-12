package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 易燃设备建档统计
 * @date 2023/9/8 13:42
 */
@Data
public class InflammableDeviceStatisticsVO {
    /**
     * 设备总数
     */
    private Integer totalNum;

    /**
     * 设备类型占比
     */
    private List<InflammableDeviceType> deviceTypeList;

    /**
     * 市场设备数
     */
    private List<MarketInflammableDevice> marketInflammableDeviceList;

    @Data
    public static class InflammableDeviceType {
        /**
         * 设备类型
         */
        private String deviceType;
        /**
         * 设备类型占比
         */
        private Double ratio;
    }

    @Data
    public static class MarketInflammableDevice {
        /**
         * 市场名称
         */
        private String marketName;
        /**
         * 设备数量
         */
        private Integer deviceNum;
    }
}
