package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsModelVo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleDeviceSchemaTipsRowMapper implements RowMapper<DeviceSchemaTipsModelVo> {

    @Override
    public DeviceSchemaTipsModelVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        DeviceSchemaTipsModelVo deviceSchemaTipsModelVo = new DeviceSchemaTipsModelVo();
        deviceSchemaTipsModelVo.setTenantId(rs.getString("tenantId"));
        deviceSchemaTipsModelVo.setDevNum(rs.getString("devNum"));
        deviceSchemaTipsModelVo.setDevName(rs.getString("devName"));
        deviceSchemaTipsModelVo.setRoot(rs.getBoolean("isRoot"));
        deviceSchemaTipsModelVo.setSchema(rs.getString("schema"));
        deviceSchemaTipsModelVo.setParentDevNum(rs.getString("parentDevNum"));
        deviceSchemaTipsModelVo.setParentDevName(rs.getString("parentDevName"));
        deviceSchemaTipsModelVo.setInsertTime(rs.getTimestamp("insert_time"));

        return deviceSchemaTipsModelVo;
    }
}
