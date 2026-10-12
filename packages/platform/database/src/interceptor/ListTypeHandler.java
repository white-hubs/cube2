package com.guhecloud.platform.database.interceptor;

import com.guhecloud.platform.base.util.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListTypeHandler extends BaseTypeHandler<List<Long>> {

    private static final String SEPARATOR = "-";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        if (!CollectionUtils.isEmpty(parameter)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int separatorNum = 0; i < parameter.size(); separatorNum++) {
                stringBuilder.append(parameter.get(separatorNum).toString());
                if (separatorNum != parameter.size() - 1) {
                    stringBuilder.append(SEPARATOR);
                }
            }
            ps.setString(i, stringBuilder.toString());
        }
    }

    @Override
    public List getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToList(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToList(cs.getString(columnIndex));
    }

    private List<Long> stringToList(String paramStr) {
        if (StringUtils.isEmpty(paramStr)) {
            return Collections.emptyList();
        }
        String[] params = paramStr.split(SEPARATOR);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(params));
        return list.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
