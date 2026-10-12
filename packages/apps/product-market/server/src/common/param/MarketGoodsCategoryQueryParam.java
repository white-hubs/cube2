package com.guhecloud.cube.srv.gen.product.market.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 商品品类
 * @date 2023-05-10 15:08:36
 */
@Data
public class MarketGoodsCategoryQueryParam extends Paging {
    //商品分类编号
    private String code;
    //商品分类名称
    private String name;
}