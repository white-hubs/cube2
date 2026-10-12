package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 储量档案扫描记录。 */
@Data
public class WarehouseStorageArchiveScanRecordVO {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

    private LocalDateTime scanTime;

    private BigDecimal currentVolume;

    private BigDecimal volumeChange;

    private BigDecimal volumeWarningValue;

    private BigDecimal volumeDifference;

    private BigDecimal volumeDifferenceWarningValue;

    private String status;

    private String statusName;
}
