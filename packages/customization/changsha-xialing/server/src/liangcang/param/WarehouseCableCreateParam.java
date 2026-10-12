package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

/** 新增粮仓电缆。 */
@Data
public class WarehouseCableCreateParam {
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 业务电缆编号。 */
    private String cableNo;
    /** 点位数量。 */
    private Integer pointCount;
}
