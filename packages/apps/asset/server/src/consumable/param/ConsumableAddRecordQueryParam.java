package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材入库
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableAddRecordQueryParam extends Paging {

    //仓库ID
    private Long storeId;
    //入库日期
    private LocalDateTime addDate;
    /**
     * 入库单号、耗材名称
     */
    private String keyword;
    /**
     * 状态
     */
    private String status;

}