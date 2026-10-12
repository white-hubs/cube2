package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleDeviceEventRowMapper implements RowMapper<DeviceEventHistoryResultVo> {
    @Override
    public DeviceEventHistoryResultVo mapRow(ResultSet rs, int i) throws SQLException {
        DeviceEventHistoryResultVo productEventLog = new DeviceEventHistoryResultVo();
        productEventLog.setDeviceId(rs.getLong("deviceId"));
        productEventLog.setEventCode(rs.getString("eventCode"));
        productEventLog.setInstanceId(rs.getString("instanceId"));
        productEventLog.setEventTime(rs.getTimestamp("eventTime").toLocalDateTime());
        productEventLog.setEventTimeStr(DateUtils.format(productEventLog.getEventTime()));
        productEventLog.setBizType(rs.getString("bizType"));
        productEventLog.setCategory(rs.getString("category"));
        return productEventLog;
    }
}
