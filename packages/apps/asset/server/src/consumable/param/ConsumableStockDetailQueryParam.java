package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 出入库记录明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableStockDetailQueryParam extends Paging {

    //仓库ID
    private Long storeId;
    //出入库标记(ADD/REDUCE)
    private String stockFlag;
    //关键字
    private String keyword;

    private Period period;

    private Long categoryId;
    private String begin;

}