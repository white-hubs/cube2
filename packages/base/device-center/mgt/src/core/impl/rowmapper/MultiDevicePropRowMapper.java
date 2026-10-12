package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 多设备属性历史查询RowMapper
 */
public class MultiDevicePropRowMapper extends SingleDevicePropRowMapper implements RowMapper<DevicePropHistoryResultVo> {

    @Override
    public DevicePropHistoryResultVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        DevicePropHistoryResultVo vo = super.mapRow(rs, rowNum);
        vo.setDeviceId(rs.getString("device_id"));
        // 兼容部分SQL返回ts为DateTime但不带date字段
        if (vo.getDate() == null && rs.getTimestamp("ts") != null) {
            vo.setDate(DateUtils.format(rs.getTimestamp("ts").toLocalDateTime()));
        }
        return vo;
    }
}

