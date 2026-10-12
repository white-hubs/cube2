package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 粮仓电缆列表项。 */
@Data
public class WarehouseCableListItemVO {
    private Long cableId;
    private Long warehouseId;
    private String cableNo;
    private Integer pointCount;
    private Integer mappedCount;
    /** mapped / partial / unmapped。 */
    private String status;
}
