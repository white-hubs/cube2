package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteResultSetHandler implements ResultSetHandler<List<PatrolRouteVO>> {
    @Override
    public List<PatrolRouteVO> handle(ResultSet rs) throws SQLException {
        List<PatrolRouteVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolRouteVO data = new PatrolRouteVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setLineId(rs.getString("line_id"));
            data.setPointId(rs.getString("place_id"));
            data.setOrder(rs.getInt("line_order"));
            list.add(data);
        }
        return list;
    }
}
