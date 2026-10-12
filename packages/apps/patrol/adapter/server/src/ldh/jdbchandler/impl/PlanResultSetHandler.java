package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanResultSetHandler implements ResultSetHandler<List<PatrolPlanVO>> {
    @Override
    public List<PatrolPlanVO> handle(ResultSet rs) throws SQLException {
        List<PatrolPlanVO> planList = new ArrayList<>();
        while (rs.next()) {
            PatrolPlanVO plan = new PatrolPlanVO();
            plan.setId(String.valueOf(rs.getInt("id")));
            plan.setPlanName(rs.getString("plan_name"));
            plan.setStartTime(DateUtils.parse(rs.getString("plan_startTime")));
            plan.setEndTime(DateUtils.parse(rs.getString("plan_endTime")));
            plan.setLineId(rs.getString("line_id"));
            plan.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            planList.add(plan);
        }
        return planList;
    }
}
