package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmResultSetHandler implements ResultSetHandler<List<PatrolAlarmVO>> {
    @Override
    public List<PatrolAlarmVO> handle(ResultSet rs) throws SQLException {
        List<PatrolAlarmVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolAlarmVO data = new PatrolAlarmVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setAlarmType(rs.getString("alarm_type"));
            data.setAlarmPoint(rs.getString("alarm_place"));
            data.setAlarmContent(rs.getString("alarm_content"));
            data.setAlarmTime(rs.getString("alarm_time"));
            data.setAlarmResult(rs.getString("alarm_result"));
            data.setPatrolRecordId(rs.getString("data_id"));
            list.add(data);
        }
        return list;
    }
}
