package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.ResultSetHandler;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuantityListResultSetHandler implements ResultSetHandler<List<IncomingQuantity>> {
    @Override
    public List<IncomingQuantity> handle(ResultSet rs) throws SQLException {
        List<IncomingQuantity> quantityList = new ArrayList<>();
        while (rs.next()) {
            IncomingQuantity quantity = new IncomingQuantity();
            quantity.setId(IdWorker.getId());
            quantity.setDateNo(rs.getString("date_no"));
            quantity.setCustId(rs.getString("cust_id"));
            quantity.setCustName(rs.getString("cust_name"));
            quantity.setGoodsCategoryId(rs.getString("enter_offer_id"));
            quantity.setIncomingQuantity(rs.getBigDecimal("enter_number"));
            quantity.setUnit(rs.getString("unit"));
            EntityAuditor.populateAudit(quantity, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM,
                                        rs.getTimestamp("load_date").toLocalDateTime());
            quantityList.add(quantity);
        }
        return quantityList;
    }
}
