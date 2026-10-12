package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.enumeration.PatrolDeviceStatusEnum;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceResultSetHandler implements ResultSetHandler<List<PatrolDeviceVO>> {
    @Override
    public List<PatrolDeviceVO> handle(ResultSet rs) throws SQLException {
        List<PatrolDeviceVO> list = new ArrayList<>();
        while (rs.next()) {
            PatrolDeviceVO data = new PatrolDeviceVO();
            data.setId(String.valueOf(rs.getInt("id")));
            data.setName(rs.getString("device_name"));
            data.setCode(rs.getString("device_number"));
            data.setDeptId(rs.getString("department_number"));
            String state = rs.getString("device_state");
            if (isChinese(state)) {
                data.setStatus(PatrolDeviceStatusEnum.getCodeByLabel(state));
                data.setStatusName(state);
            } else {
                data.setStatus(state);
                data.setStatusName(PatrolDeviceStatusEnum.getLabelByCode(state));
            }
            data.setUser(rs.getString("bind_user"));
            list.add(data);
        }
        return list;
    }

    /**
     * 判断字符串是否为中文
     *
     * @param str
     *
     * @return
     */
    public boolean isChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
