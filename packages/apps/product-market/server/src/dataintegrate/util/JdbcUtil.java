package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.util;

import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.handler.ResultSetHandler;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.util.dbbase.MysqlConnect;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.util.dbbase.OracleConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class JdbcUtil {
    private static MysqlConnect mysqlConnect;
    private static OracleConnect oracleConnect;

    @Autowired
    private void setMysqlConnect(MysqlConnect mysqlConnect) {
        JdbcUtil.mysqlConnect = mysqlConnect;
    }

    @Autowired
    private void setOracleConnect(OracleConnect oracleConnect) {
        JdbcUtil.oracleConnect = oracleConnect;
    }

    // 加载驱动并返回连接对象
    public static Connection getMysqlConnection() throws SQLException {
        String url = "jdbc:mysql://" + mysqlConnect.getIp() + ":" + mysqlConnect.getPort() + "/" + mysqlConnect.getDb() +
                     "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String username = mysqlConnect.getUsername();
        String password = mysqlConnect.getPassword();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getOracleConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@" + oracleConnect.getIp() + ":" + oracleConnect.getPort() + ":" + oracleConnect.getDb() + "";
        String username = oracleConnect.getUsername();
        String password = oracleConnect.getPassword();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }

    // 关闭连接、语句和结果集等资源
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
            e.printStackTrace();
        }
    }

    // 执行查询，并返回指定类型的结果
    public static <T> T executeMysqlQuery(String sql, Object[] params, ResultSetHandler<T> handler) {
        Connection connection = null;
        try {
            connection = JdbcUtil.getMysqlConnection();
            return executeQuery(connection, sql, params, handler);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, null, null);
        }
        return null;
    }

    public static <T> T executeOracleQuery(String sql, Object[] params, ResultSetHandler<T> handler) {
        Connection connection = null;
        try {
            connection = JdbcUtil.getOracleConnection();
            return executeQuery(connection, sql, params, handler);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, null, null);
        }
        return null;
    }

    public static <T> T executeQuery(Connection connection, String sql, Object[] params, ResultSetHandler<T> handler) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        try {
            ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            result = handler.handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, ps, rs);
        }

        return result;
    }
}

