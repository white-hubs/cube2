package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl;

import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.ResultSetHandler;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoodsCategoryResultSetHandler implements ResultSetHandler<List<MarketGoodsCategory>> {
    @Override
    public List<MarketGoodsCategory> handle(ResultSet rs) throws SQLException {
        List<MarketGoodsCategory> categoryList = new ArrayList<>();
        while (rs.next()) {
            MarketGoodsCategory category = new MarketGoodsCategory();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));
            category.setCode(rs.getString("id"));
            long parentId = rs.getLong("type_id");
            category.setParentId(parentId == 0 ? null : parentId);
            category.setEnabled(true);
            category.setDeleted(false);
            EntityAuditor.populateAudit(category, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
            categoryList.add(category);
        }
        return categoryList;
    }
}
