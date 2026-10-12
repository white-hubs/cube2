package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 货位卡字段变更记录查询参数。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseLocationCardChangeLogQueryParam extends Paging {
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 业务类型。 */
    private String bizType;
}
