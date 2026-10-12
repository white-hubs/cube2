package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.QueryCondition;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class LineLossClickHouseServiceImpl implements LineLossClickHouseService {

    private final JdbcTemplate jdbcTemplate;

    public LineLossClickHouseServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 分页查询
     *
     * @param tableName    表名
     * @param clazz        返回结果对象类型
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param deviceIdSet  设备 ID 集合
     * @param pageNumber   页码
     * @param pageSize     每页记录数
     * @param orderByField 排序字段，为空则默认按创建时间排序
     *
     * @return 分页结果
     */
    @Override
    public <T> Pagination<T> pageClickHouse(String tableName, Class<T> clazz, String startTime, String endTime, Set<Long> deviceIdSet,
                                            long pageNumber,
                                            long pageSize,
                                            String orderByField
                                           ) {
        // 构建查询条件
        QueryCondition queryCondition = buildQueryCondition(startTime, endTime, deviceIdSet);

        // 添加分页参数
        List<Object> params = new ArrayList<>(queryCondition.getParams());
        long offset = (pageNumber - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);

        // 构建排序子句
        String orderByClause = buildOrderByClause(orderByField);

        // 构建 SQL
        String selectSql = "select * from " + tableName + " " + queryCondition.getWhereClause() + " " + orderByClause + " limit ? offset ?";
        String countSql = "select count(1) from " + tableName + " " + queryCondition.getWhereClause();

        try {
            // 查询总记录数
            Long total = jdbcTemplate.queryForObject(countSql, Long.class, queryCondition.getParams().toArray());

            if (total == null || total == 0) {
                return Pagination.empty();
            }

            // 计算总页数
            long pages = (total + pageSize - 1) / pageSize;

            // 查询当页数据
            List<T> records = queryForList(selectSql, clazz, params.toArray());

            // 构建分页对象
            return Pagination.<T>builder()
                    .total(total)
                    .pages(pages)
                    .records(records)
                    .build();

        } catch (Exception e) {
            log.error("ClickHouse 分页查询失败，tableName: {}, startTime: {}, endTime: {}, deviceIdSet: {}",
                      tableName, startTime, endTime, deviceIdSet, e);
            throw new RuntimeException("ClickHouse 分页查询失败", e);
        }
    }

    /**
     * 查询列表（不分页）
     *
     * @param tableName    表名
     * @param clazz        返回结果对象类型
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param deviceIdSet  设备 ID 集合
     * @param orderByField 排序字段，为空则默认按创建时间排序
     *
     * @return 数据列表
     */
    @Override
    public <T> List<T> listClickHouse(String tableName, Class<T> clazz, String startTime, String endTime, Set<Long> deviceIdSet,
                                      String orderByField
                                     ) {
        // 构建查询条件
        QueryCondition queryCondition = buildQueryCondition(startTime, endTime, deviceIdSet);

        // 构建排序子句
        String orderByClause = buildOrderByClause(orderByField);

        // 构建 SQL（不分页）
        String selectSql = "select * from " + tableName + " " + queryCondition.getWhereClause() + " " + orderByClause;

        try {
            // 查询数据列表
            return queryForList(selectSql, clazz, queryCondition.getParams().toArray());

        } catch (Exception e) {
            log.error("ClickHouse 列表查询失败，tableName: {}, startTime: {}, endTime: {}, deviceIdSet: {}",
                      tableName, startTime, endTime, deviceIdSet, e);
            throw new RuntimeException("ClickHouse 列表查询失败", e);
        }
    }

    /**
     * 查询最新一条数据
     *
     * @param tableName  表名
     * @param clazz      返回结果对象类型
     * @param whereIdSet 条件 ID 集合
     *
     * @return 最新数据列表
     */
    @Override
    public <T> List<T> listLatestByWhereColumn(String tableName, Class<T> clazz, Set<Long> whereIdSet, String column) {
        if (whereIdSet == null || whereIdSet.isEmpty()) {
            log.warn("查询每个最新数据，ID 集合为空，tableName: {}", tableName);
            return CollectionUtils.emptyList();
        }

        try {
            // 构建 ID 过滤条件
            StringBuilder whereIdsClause = new StringBuilder();
            List<Object> params = new ArrayList<>();
            for (int i = 0; i < whereIdSet.size(); i++) {
                if (i > 0) {
                    whereIdsClause.append(",");
                }
                whereIdsClause.append("?");
                params.add(whereIdSet.toArray()[i]);
            }

            // 使用子查询获取每个条件的最大 create_time，然后关联查询完整数据
            String selectSql = """
                    SELECT t1.*
                    FROM %s t1
                    INNER JOIN (
                        SELECT %s, MAX(create_time) as max_time
                        FROM %s
                        WHERE %s IN (%s)
                        GROUP BY %s
                    ) t2 ON t1.%s = t2.%s AND t1.create_time = t2.max_time
                    """.formatted(tableName, column, tableName, column, whereIdsClause, column, column, column);

            // 查询数据列表
            return queryForList(selectSql, clazz, params.toArray());

        } catch (Exception e) {
            log.error("查询最新数据失败，tableName: {}, whereIdSet: {}",
                      tableName, whereIdSet, e);
            throw new RuntimeException("查询最新数据失败", e);
        }
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据列表
     *
     * @return 插入的行数
     */
    @Override
    public <T> int batchInsertClickHouse(String tableName, List<T> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            log.warn("批量插入数据为空，tableName：{}", tableName);
            return 0;
        }

        try {
            T firstData = dataList.getFirst();
            Class<?> clazz = firstData.getClass();
            var fields = getRelevantFields(clazz);

            // 构建 INSERT SQL
            String insertSql = buildInsertSql(tableName, fields);

            // 分批插入，每批 100 条
            int batchSize = 100;
            int totalRows = 0;
            int dataSize = dataList.size();

            for (int i = 0; i < dataSize; i += batchSize) {
                int end = Math.min(i + batchSize, dataSize);
                List<T> subList = new ArrayList<>(dataList.subList(i, end));

                // 执行批量插入
                int[] results = executeBatchInsert(insertSql, subList, fields);

                // 累加插入行数
                totalRows += calculateTotalRows(results);
            }

            return totalRows;

        } catch (Exception e) {
            log.error("批量插入失败，tableName: {}, 数据盘：{}", tableName, dataList.size(), e);
            throw new RuntimeException("批量插入失败", e);
        }
    }

    /**
     * 通用查询方法 - 允许调用方自定义查询条件
     *
     * @param tableName      表名
     * @param clazz          返回结果对象类型
     * @param queryCondition 自定义查询条件
     * @param orderByField   排序字段，为空则默认按创建时间倒序
     *
     * @return 数据列表
     */
    public <T> List<T> listClickHouse(String tableName, Class<T> clazz, QueryCondition queryCondition, String orderByField) {
        // 构建排序子句
        String orderByClause = buildOrderByClause(orderByField);

        // 构建 SQL（不分页）
        String selectSql = "select * from " + tableName + " " + queryCondition.getWhereClause() + " " + orderByClause;

        try {
            // 查询数据列表
            return queryForList(selectSql, clazz, queryCondition.getParams().toArray());

        } catch (Exception e) {
            log.error("ClickHouse 列表查询失败，tableName: {}, queryCondition: {}",
                      tableName, queryCondition, e);
            throw new RuntimeException("ClickHouse 列表查询失败", e);
        }
    }

    /**
     * 通用分页查询方法 - 允许调用方自定义查询条件
     *
     * @param tableName      表名
     * @param clazz          返回结果对象类型
     * @param queryCondition 自定义查询条件
     * @param orderByField   排序字段，为空则默认按创建时间倒序
     * @param pageNumber     页码
     * @param pageSize       每页记录数
     *
     * @return 分页结果
     */
    public <T> Pagination<T> pageClickHouse(String tableName, Class<T> clazz, QueryCondition queryCondition,
                                            String orderByField, long pageNumber, long pageSize
                                           ) {
        // 添加分页参数
        List<Object> params = new ArrayList<>(queryCondition.getParams());
        long offset = (pageNumber - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);

        // 构建排序子句
        String orderByClause = buildOrderByClause(orderByField);

        // 构建 SQL
        String selectSql = "select * from " + tableName + " " + queryCondition.getWhereClause() + " " + orderByClause + " limit ? offset ?";
        String countSql = "select count(1) from " + tableName + " " + queryCondition.getWhereClause();

        try {
            // 查询总记录数
            Long total = jdbcTemplate.queryForObject(countSql, Long.class, queryCondition.getParams().toArray());

            if (total == null || total == 0) {
                return Pagination.empty();
            }

            // 计算总页数
            long pages = (total + pageSize - 1) / pageSize;

            // 查询当页数据
            List<T> records = queryForList(selectSql, clazz, params.toArray());

            // 构建分页对象
            return Pagination.<T>builder()
                    .total(total)
                    .pages(pages)
                    .records(records)
                    .build();

        } catch (Exception e) {
            log.error("ClickHouse 分页查询失败，tableName: {}, queryCondition: {}",
                      tableName, queryCondition, e);
            throw new RuntimeException("ClickHouse 分页查询失败", e);
        }
    }

    /**
     * 构建查询条件和参数（便捷方法 - 默认按时间范围和设备 ID 查询）
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param deviceIdSet 设备 ID 集合
     *
     * @return 查询条件对象
     */
    public QueryCondition buildQueryCondition(String startTime, String endTime, Set<Long> deviceIdSet) {
        StringBuilder whereClause = new StringBuilder("where create_time >= ? and create_time <= ?");
        List<Object> params = new ArrayList<>();
        params.add(startTime);
        params.add(endTime);

        // 添加设备 ID 过滤条件
        if (deviceIdSet != null && !deviceIdSet.isEmpty()) {
            whereClause.append(" and device_id in (");
            for (int i = 0; i < deviceIdSet.size(); i++) {
                if (i > 0) {
                    whereClause.append(",");
                }
                whereClause.append("?");
                params.add(deviceIdSet.toArray()[i]);
            }
            whereClause.append(")");
        }

        return new QueryCondition(whereClause.toString(), params);
    }

    /**
     * 构建排序子句
     *
     * @param orderByField 排序字段
     *
     * @return 排序子句，如果字段为空则默认按创建时间倒序
     */
    private String buildOrderByClause(String orderByField) {
        if (orderByField == null || orderByField.trim().isEmpty()) {
            return "order by create_time desc";
        }
        return "order by " + orderByField + " desc";
    }

    /**
     * 执行查询并映射为对象列表
     *
     * @param sql    SQL 语句
     * @param clazz  对象类型
     * @param params 参数数组
     *
     * @return 对象列表
     */
    private <T> List<T> queryForList(String sql, Class<T> clazz, Object... params) {
        return jdbcTemplate.query(
                sql,
                new RowMapper<T>() {
                    @Override
                    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return BeanPropertyRowMapper.newInstance(clazz).mapRow(rs, rowNum);
                    }
                },
                params
                                 );
    }

    /**
     * 获取相关字段数组（过滤掉 serialVersionUID 等无关字段）
     *
     * @param clazz 类
     *
     * @return 相关字段数组
     */
    private Field[] getRelevantFields(Class<?> clazz) {
        var allFields = clazz.getDeclaredFields();
        return java.util.Arrays.stream(allFields)
                .filter(field -> !"serialVersionUID".equals(field.getName()))
                .toArray(Field[]::new);
    }

    /**
     * 构建 INSERT SQL 语句
     *
     * @param tableName 表名
     * @param fields    字段数组
     *
     * @return INSERT SQL 语句
     */
    private String buildInsertSql(String tableName, Field[] fields) {
        StringBuilder columnsSb = new StringBuilder();
        StringBuilder valuesSb = new StringBuilder();
        boolean firstColumn = true;

        for (var field : fields) {
            if (!firstColumn) {
                columnsSb.append(", ");
                valuesSb.append(", ");
            }

            columnsSb.append(camelToUnderline(field.getName()));
            valuesSb.append("?");
            firstColumn = false;
        }

        return "INSERT INTO " + tableName + " (" + columnsSb.toString() + ") VALUES (" + valuesSb.toString() + ")";
    }

    /**
     * 执行批量插入
     *
     * @param insertSql INSERT SQL
     * @param dataList  数据列表
     * @param fields    字段数组
     *
     * @return 批量插入结果数组
     */
    private <T> int[] executeBatchInsert(String insertSql, List<T> dataList, Field[] fields) {
        return jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                setPreparedStatementValues(ps, dataList.get(i), fields);
            }

            @Override
            public int getBatchSize() {
                return dataList.size();
            }
        });
    }

    /**
     * 设置 PreparedStatement 参数值
     *
     * @param ps     PreparedStatement
     * @param data   数据对象
     * @param fields 字段数组
     */
    private <T> void setPreparedStatementValues(PreparedStatement ps, T data, Field[] fields) throws SQLException {
        var beanWrapper = new BeanWrapperImpl(data);
        int paramIndex = 0;

        for (var field : fields) {
            Object value = beanWrapper.getPropertyValue(field.getName());
            if (value != null) {
                ps.setObject(paramIndex + 1, value);
            } else {
                ps.setNull(paramIndex + 1, java.sql.Types.NULL);
            }
            paramIndex++;
        }
    }

    /**
     * 计算总插入行数
     *
     * @param results 批量插入结果数组
     *
     * @return 总行数
     */
    private int calculateTotalRows(int[] results) {
        int totalRows = 0;
        for (int result : results) {
            if (result > 0) {
                totalRows += result;
            }
        }
        return totalRows;
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param camel 驼峰命名的字符串
     *
     * @return 下划线命名的字符串
     */
    private String camelToUnderline(String camel) {
        if (camel == null || camel.isEmpty()) {
            return camel;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : camel.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
