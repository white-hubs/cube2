package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}
