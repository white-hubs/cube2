package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service.impl;

import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl.GoodsCategoryResultSetHandler;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl.QuantityListResultSetHandler;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl.StallListResultSetHandler;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service.ThirdDataQueryService;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.util.JdbcUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdDataQueryServiceImpl implements ThirdDataQueryService {
    @Override
    public List<MarketStall> getStalls() {
        //查询第三方档位数据
        String sql = "select * from basic_shop_stall limit 10";
        List<MarketStall> marketStalls = JdbcUtil.executeMysqlQuery(sql, null, new StallListResultSetHandler());
        return marketStalls;
    }

    @Override
    public List<IncomingQuantity> getIncomingQuantity(String date) {
        String sql = "select * from DM_OFFER_CUST_BEH_D where date_no = " + date;
        List<IncomingQuantity> incomingQuantities = JdbcUtil.executeOracleQuery(sql, null, new QuantityListResultSetHandler());
        return incomingQuantities;
    }

    @Override
    public List<MarketGoodsCategory> getGoodsCategory() {
        //查询第三方品类数据
        String sql = "select * from basic_goods where id = 125948 or type_id = 125948";
        List<MarketGoodsCategory> categoryList = JdbcUtil.executeMysqlQuery(sql, null, new GoodsCategoryResultSetHandler());
        return categoryList;
    }
}
