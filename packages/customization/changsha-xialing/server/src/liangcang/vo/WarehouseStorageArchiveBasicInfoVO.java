package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 储量档案基本信息。 */
@Data
public class WarehouseStorageArchiveBasicInfoVO {

    private Long warehouseId;

    private String warehouseName;

    private String grainType;

    private BigDecimal currentVolume;

    private BigDecimal volumeWarningValue;

    private BigDecimal volumeDifferenceWarningValue;

    private LocalDateTime latestScanTime;
}
