package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理器接口
 */
public interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}
