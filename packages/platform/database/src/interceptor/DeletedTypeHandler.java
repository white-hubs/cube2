package com.guhecloud.platform.database.interceptor;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 是已删除字段类型处理实现
 * <p>
 * 当删除标记为true时，将字段值设置为当前时间戳，用于与其他字段一起用作唯一索引时排除已删除的数据
 *
 * @author Woody
 * @date 2021/7/17
 * @see com.guhecloud.platform.database.entity.BasicEntity
 */
public class DeletedTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setLong(i, Boolean.TRUE.equals(parameter) ? System.currentTimeMillis() : 0);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getBoolean(columnName);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getBoolean(columnIndex);
    }
}
