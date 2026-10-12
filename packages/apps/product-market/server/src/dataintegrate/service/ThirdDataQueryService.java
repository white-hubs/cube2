package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service;

import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;

import java.util.List;

public interface ThirdDataQueryService {
    /**
     * 获取档位数据
     *
     * @return
     */
    List<MarketStall> getStalls();

    List<IncomingQuantity> getIncomingQuantity(String date);

    List<MarketGoodsCategory> getGoodsCategory();
}
