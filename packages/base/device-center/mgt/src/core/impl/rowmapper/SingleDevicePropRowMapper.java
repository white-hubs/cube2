package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleDevicePropRowMapper implements RowMapper<DevicePropHistoryResultVo> {

    @Override
    public DevicePropHistoryResultVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        String propDataType = rs.getString("prop_data_type");
        String propCode = rs.getString("prop_code");
        String ts = DateUtils.format(rs.getTimestamp("ts").toLocalDateTime());
        Object value;
        IotDataType dataTypeEnum;
        try {
            dataTypeEnum = IotDataType.fromCode(propDataType);
        } catch (IllegalArgumentException e) {
            dataTypeEnum = null;
        }

        if (dataTypeEnum == null) {
            value = rs.getString("string_value");
        } else {
            switch (dataTypeEnum) {
                case INT:
                case LONG:
                    value = rs.getInt("int_value");
                    break;
                case FLOAT:
                case DOUBLE:
                    // 使用字符串避免精度丢失
                    value = rs.getString("string_value");
                    break;
                case ENUM:
                case BOOLEAN:
                case STRING:
                default:
                    value = rs.getString("string_value");
            }
        }

        DevicePropHistoryResultVo historyResultVo = new DevicePropHistoryResultVo();
        historyResultVo.setValue(value);
        historyResultVo.setPropCode(propCode);
        historyResultVo.setValueOfStr(rs.getString("string_value"));
        historyResultVo.setDate(ts);
        historyResultVo.setTs(rs.getTimestamp("ts").getTime());
        return historyResultVo;
    }
}
