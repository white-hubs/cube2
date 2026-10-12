package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价记录
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingRecordQueryParam extends Paging {
    //关键字
    private String keyword;
    //商品品类ID
    private Long goodsCategoryId;
    //品类名称
    private String goodsCategoryName;
    //采价时间范围
    private Period period;
    //任务ID
    private Long taskId;

}