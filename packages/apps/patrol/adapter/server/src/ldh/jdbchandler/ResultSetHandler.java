package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}
