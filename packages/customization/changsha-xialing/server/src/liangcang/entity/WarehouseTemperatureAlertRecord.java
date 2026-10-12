package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓温度趋势预警记录。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_alert_record")
public class WarehouseTemperatureAlertRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private String warehouseName;
    private String grainType;
    private LocalDateTime triggeredAt;
    private BigDecimal currentAvgTemperature;
    private BigDecimal warningThreshold;
    private BigDecimal approachMargin;
    private BigDecimal distanceToThreshold;
    private BigDecimal dailyIncrease;
    private Integer consecutiveDays;
    private String status;
    private LocalDateTime clearedAt;
    private String clearedReason;
    private Boolean popupEnabled;
    private Boolean popupPushed;
}
