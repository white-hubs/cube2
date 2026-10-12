package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.impl;

import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.ResultSetHandler;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StallListResultSetHandler implements ResultSetHandler<List<MarketStall>> {
    @Override
    public List<MarketStall> handle(ResultSet rs) throws SQLException {
        List<MarketStall> stallList = new ArrayList<>();
        while (rs.next()) {
            MarketStall stall = new MarketStall();
            stall.setId(rs.getLong("id"));
            stall.setName(rs.getString("stall_name"));
            stall.setCode(rs.getString("stall_code"));
            stall.setType(rs.getString("stall_type_id"));
            stall.setLocation(rs.getString("addr_detail"));
            stall.setBossId(rs.getString("boss_id"));
            stall.setBossName(rs.getString("boss_name"));
            stall.setBossCardNo(rs.getString("boss_card_no"));
            stall.setBossPhone(rs.getString("boss_phone"));
            stall.setDeleted(false);
            EntityAuditor.populateAudit(stall, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
            stallList.add(stall);
        }
        return stallList;
    }
}
