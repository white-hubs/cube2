package com.guhecloud.cube.base.device.center.core.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanToIntTypeHandler extends BaseTypeHandler<Boolean> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        // 将 Boolean 转换为 INT (true -> 1, false -> 0)
        ps.setInt(i, parameter ? 1 : 0);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 将 INT 转换为 Boolean (1 -> true, 0 -> false)
        int value = rs.getInt(columnName);
        return value != 0;
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return value != 0;
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return value != 0;
    }
}
