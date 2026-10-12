package com.guhecloud.cube.iot.mgt.comm.mybatis;

import com.guhecloud.platform.base.util.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectArrayTypeHandler extends BaseTypeHandler<Object[]> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object[] objects, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, StringUtils.join(",", objects).replace("\"", ""));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return StringUtils.split(resultSet.getString(columnName), ",").toArray();
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return StringUtils.split(resultSet.getString(columnIndex), ",").toArray();
    }

    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return StringUtils.split(callableStatement.getString(columnIndex), ",").toArray();
    }
}