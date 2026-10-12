package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AppConsumablePageParam extends Paging {
    private Long id;
    /**
     * 盘点计划id
     */
    private Long checkId;
    private Long storeId;
    /**
     * 盘点状态
     */
    private String status;
}
