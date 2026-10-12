package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PointResultSetHandler implements ResultSetHandler<List<PatrolPointVO>> {
    @Override
    public List<PatrolPointVO> handle(ResultSet rs) throws SQLException {
        List<PatrolPointVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolPointVO data = new PatrolPointVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setName(rs.getString("checkPoint_name"));
            data.setCode(rs.getString("checkPoint_card"));
            data.setType(rs.getString("checkPoint_type"));
            data.setLongitude(rs.getString("gps_longitude"));
            data.setLatitude(rs.getString("gps_latitude"));
            list.add(data);
        }
        return list;
    }
}
