package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.util;

import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.config.HrDatabaseConfig;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.handler.ResultSetHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * HR数据库JDBC工具类
 */
@Slf4j
public class HrJdbcUtil {

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(HrDatabaseConfig config) throws SQLException {
        try {
            Class.forName(config.getDriverClass());
        } catch (ClassNotFoundException e) {
            log.error("加载数据库驱动失败，驱动类: {}", config.getDriverClass(), e);
            throw new SQLException(String.format("加载数据库驱动失败，驱动类: %s，请检查驱动依赖是否正确添加", config.getDriverClass()), e);
        }
        return DriverManager.getConnection(config.getConnectionUrl(), config.getUsername(), config.getPassword());
    }

    /**
     * 关闭数据库资源
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("关闭数据库资源失败", e);
        }
    }

    /**
     * 执行查询
     */
    public static <T> T executeQuery(HrDatabaseConfig config, String sql, Object[] params, ResultSetHandler<T> handler) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = getConnection(config);
            ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            return handler.handle(rs);
        } catch (Exception e) {
            log.error("执行查询失败，SQL: {}", sql, e);
            throw new RuntimeException("执行查询失败", e);
        } finally {
            close(connection, ps, rs);
        }
    }
}
