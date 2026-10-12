package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolLineVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineResultSetHandler implements ResultSetHandler<List<PatrolLineVO>> {
    @Override
    public List<PatrolLineVO> handle(ResultSet rs) throws SQLException {
        List<PatrolLineVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolLineVO data = new PatrolLineVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setName(rs.getString("line_name"));
            list.add(data);
        }
        return list;
    }
}
