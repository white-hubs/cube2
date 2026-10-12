package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** 货量唯一键：仓 id + 采集时间。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseGoodsAcquireKey {
    private Long warehouseId;
    private LocalDateTime acquisitionDate;
}
