package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 货位卡综合信息
 * 智慧仓档案、各类设备在离线统计和货位卡配置详情，供货位卡页面展示。
 */
@Data
public class LiangcangLocationCardInfoVO {

    /** 智慧仓档案（来自三方 warehouse 匹配结果）。 */
    private WarehouseInfo warehouse;
    /** 温度设备在离线数量（依据设备中心 post 详情中的 device_state）。 */
    private OnlineOfflineStats temperatureDeviceStats;
    /** 湿度设备在离线数量。 */
    private OnlineOfflineStats humidityDeviceStats;
    /** 气体类设备在离线数量。 */
    private OnlineOfflineStats gasDeviceStats;
    /** 雷达设备在离线数量（绑定表 radar_warehouse_binding 中的雷达设备 id）。 */
    private OnlineOfflineStats radarDeviceStats;
    /** 货位卡配置详情。 */
    private WarehouseLocationCardConfigDetailVO configDetail;
    /** 粮仓温度报表。 */
    private List<List<WarehouseTemperatureReportPointVO>> reportList;
    /** 粮仓温度趋势预警提示。 */
    private WarehouseTemperatureAlertNoticeVO temperatureAlertNotice;

    @Data
    public static class OnlineOfflineStats {
        /** 在线设备数。 */
        private int onlineCount;
        /** 离线设备数。 */
        private int offlineCount;
    }

    @Data
    public static class WarehouseInfo {
        /** 智慧仓 id。 */
        private Long warehouseId;
        /** 仓名称。 */
        private String name;
        /** 仓型或品类名称。 */
        private String categoryName;
        /** 总容积。 */
        private BigDecimal totalVolume;
        /** 储粮品质等级。 */
        private String qualityGrade;
        /** 仓房高度。 */
        private BigDecimal granaryHeight;
        /** 仓容利用率。 */
        private BigDecimal usageRate;
    }

}
