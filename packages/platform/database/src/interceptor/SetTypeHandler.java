package com.guhecloud.platform.database.interceptor;

import com.guhecloud.platform.base.util.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SetTypeHandler extends BaseTypeHandler<Set<Long>> {

    private static final String SEPARATOR = "-";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<Long> parameter, JdbcType jdbcType) throws SQLException {
        if (!CollectionUtils.isEmpty(parameter)) {
            StringBuilder stringBuilder = new StringBuilder();
            int separatorNum = 0;
            for (Object object : parameter) {
                stringBuilder.append(object.toString());
                if (separatorNum != parameter.size() - 1) {
                    stringBuilder.append(SEPARATOR);
                }
                separatorNum++;
            }
            ps.setString(i, stringBuilder.toString());
        }
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToList(rs.getString(columnName));
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToList(rs.getString(columnIndex));
    }

    @Override
    public Set<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToList(cs.getString(columnIndex));
    }

    private Set<Long> stringToList(String paramStr) {
        if (StringUtils.isEmpty(paramStr)) {
            return Collections.emptySet();
        }
        String[] params = paramStr.split(SEPARATOR);
        HashSet<String> hashSet = new HashSet<>(Arrays.asList(params));
        return hashSet.stream().map(Long::valueOf).collect(Collectors.toSet());
    }
}
