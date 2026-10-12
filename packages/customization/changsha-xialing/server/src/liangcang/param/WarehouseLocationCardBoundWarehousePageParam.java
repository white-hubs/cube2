package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 货位卡已绑定粮仓分页参数。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseLocationCardBoundWarehousePageParam extends Paging {
    /** 粮仓名称。 */
    private String warehouseName;
}
