package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAggType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** 仓库粮温趋势配置详情。 */
@Data
public class WarehouseTemperatureTrendConfigVO {
    /** 配置 ID。 */
    private Long configId;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 全仓超温预警线。 */
    private BigDecimal highAlarmThreshold;
    /** 全仓低温预警线。 */
    private BigDecimal lowAlarmThreshold;
    /** 全仓温度汇总取值规则：MAX、MIN。 */
    private WarehouseTemperatureAggType warehouseAggType;
    /** 分层配置。 */
    private List<Layer> layers;

    @Data
    public static class Layer {
        /** 分层配置 ID。 */
        private Long layerConfigId;
        /** 层级编码：UPPER、MIDDLE、LOWER。 */
        private String layerCode;
        /** 层级名称。 */
        private String layerName;
        /** 页面位置标签。 */
        private String positionName;
        /** 已选择电缆 ID。 */
        private List<Long> selectedCableIds;
        /** 超温预警线。 */
        private BigDecimal highAlarmThreshold;
        /** 低温预警线。 */
        private BigDecimal lowAlarmThreshold;
        /** 分层温度汇总取值规则：MAX、MIN。 */
        private WarehouseTemperatureAggType layerAggType;
        /** 排序号。 */
        private Integer sortNo;
    }
}
