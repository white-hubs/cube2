package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡更记录结果处理
 */
public class RecordResultSetHandler implements ResultSetHandler<List<PatrolRecordVO>> {
    @Override
    public List<PatrolRecordVO> handle(ResultSet rs) throws SQLException {
        List<PatrolRecordVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolRecordVO data = new PatrolRecordVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setStartTime(rs.getTimestamp("plan_startTime").toLocalDateTime());
            data.setEndTime(rs.getTimestamp("plan_endTime").toLocalDateTime());
            data.setPatrolTime(rs.getTimestamp("patrol_time").toLocalDateTime());
            data.setLineName(rs.getString("line_name"));
            data.setPointName(rs.getString("place_name"));
            data.setLineOrder(rs.getLong("line_order"));
            data.setUserName(rs.getString("user_name"));
            data.setDeviceName(rs.getString("device_name"));
            data.setStatus(rs.getString("plan_state"));
            data.setStayTime(rs.getInt("stay_time"));
            data.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            data.setPlanId(rs.getString("plan_id"));
            data.setNormal(rs.getInt("dflag") == 0);
            list.add(data);
        }
        return list;
    }
}
