package com.guhecloud.cube.srv.gen.adapter.patrol.server.utils;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.ResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.model.DBConfig;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class JdbcUtil {

    // 加载驱动并返回连接对象
    public static Connection getDBConnection(DBConfig dbConfig) throws SQLException {
        try {
            Class.forName(dbConfig.getDriverClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(dbConfig.getDburl(), dbConfig.getUsername(), dbConfig.getPassword());
    }

    // 关闭连接、语句和结果集等资源
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) throws Exception {
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
            throw e;
        }
    }

    // 执行查询，并返回指定类型的结果
    public static <T> T executeQuery(DBConfig dbConfig, String sql, Object[] params, ResultSetHandler<T> handler) throws Exception {
        Connection connection = null;
        try {
            connection = JdbcUtil.getDBConnection(dbConfig);
            return executeQuery(connection, sql, params, handler);
        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtil.close(connection, null, null);
        }
    }

    public static void executeDelete(DBConfig dbConfig, String sql, Object[] params) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JdbcUtil.getDBConnection(dbConfig);
            ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ps.execute();
        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtil.close(connection, ps, null);
        }
    }

    public static <T> T executeQuery(Connection connection, String sql, Object[] params, ResultSetHandler<T> handler) throws Exception {
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
            throw e;
        } finally {
            JdbcUtil.close(connection, ps, rs);
        }

        return result;
    }

    // 执行插入或更新操作
    public static void executeInsertOrUpdate(DBConfig dbConfig, String sql, List<Object[]> data) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JdbcUtil.getDBConnection(dbConfig);
            ps = connection.prepareStatement(sql);

            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    ps.setObject(i + 1, row[i]);
                }
                ps.addBatch(); // 将当前行添加到批处理中
            }
            int[] ints = ps.executeBatch();// 执行批处理
            log.info("插入数据：" + Arrays.stream(ints).sum() + " 条");
        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtil.close(connection, ps, null);
        }
    }

    /**
     * 分页插入数据
     *
     * @param sql
     * @param datas
     *
     * @throws Exception
     */
    public static void executePageInsert(DBConfig dbConfig, String sql, List<Object[]> datas) throws Exception {
        int batchSize = 500; // 每次取出的批量大小
        int totalSize = datas.size(); // 数据总量
        int numBatches = (totalSize + batchSize - 1) / batchSize; // 计算批次数
        for (int i = 0; i < numBatches; i++) {
            int start = i * batchSize; // 当前批次的起始索引
            int end = Math.min(start + batchSize, totalSize); // 当前批次的结束索引（可能小于batchSize）

            List<Object[]> batch = datas.subList(start, end); // 获取当前批次的数据
            //插入
            JdbcUtil.executeInsertOrUpdate(dbConfig, sql, batch);
        }
    }

    public static String[] getQueryPeriod(String param) {
        String startDate = null;
        String endDate = null;
        if (StringUtils.isNotBlank(param)) {
            //传入时间段，例如：2023-08-01,2023-08-10
            String pattern = "\\d{4}-\\d{2}-\\d{2}";
            String[] split = param.split(",");
            if (split.length == 2 && Pattern.matches(pattern, split[0]) && Pattern.matches(pattern, split[1])) {
                startDate = split[0];
                endDate = split[1];
            }
        }
        if (startDate == null && endDate == null) {
            //设置前一天的时间
            var preDate = DateUtils.offsetDay(LocalDateTime.now(), -1);
            startDate = DateUtils.format(preDate, "yyyy-MM-dd");
            endDate = startDate;
        }
        return new String[]{ startDate, endDate };
    }
}

