package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.time.LocalDate;

/** 气体档案趋势查询参数。 */
@Data
public class GasArchiveTrendParam {

    private Long warehouseId;

    private LocalDate date;
}
