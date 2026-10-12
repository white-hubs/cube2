package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import java.time.LocalDateTime;

/** 测量信息采集去重键。 */
public record WarehouseMeasureAcquireKey(Long warehouseId, LocalDateTime acquisitionDate) {
}
