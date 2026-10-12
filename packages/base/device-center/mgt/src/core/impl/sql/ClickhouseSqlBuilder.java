package com.guhecloud.cube.iot.mgt.core.impl.sql;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Collection;

/**
 * ClickHouse SQL 构建器基类
 * 提供链式调用和 SQL 直接拼接支持
 */
public class ClickhouseSqlBuilder {

    protected final StringBuilder selectClause = new StringBuilder();
    protected final StringBuilder fromClause = new StringBuilder();
    protected final StringBuilder whereClause = new StringBuilder();
    protected final StringBuilder groupByClause = new StringBuilder();
    protected final StringBuilder orderByClause = new StringBuilder();
    protected final StringBuilder limitClause = new StringBuilder();

    protected boolean hasWhereCondition = false;

    // ==================== SELECT ====================

    /**
     * 设置 SELECT 子句
     *
     * @param columns 列定义
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder select(String columns) {
        selectClause.setLength(0);
        selectClause.append("SELECT ").append(columns);
        return this;
    }

    // ==================== FROM ====================

    /**
     * 设置 FROM 子句
     *
     * @param tableName 表名
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder from(String tableName) {
        fromClause.setLength(0);
        fromClause.append(" FROM ").append(tableName);
        return this;
    }

    /**
     * 设置 FROM 子句，支持 FINAL 关键字
     *
     * @param tableName 表名
     * @param useFinal  是否使用 FINAL
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder from(String tableName, boolean useFinal) {
        fromClause.setLength(0);
        fromClause.append(" FROM ").append(tableName);
        if (useFinal) {
            fromClause.append(" FINAL");
        }
        return this;
    }

    // ==================== WHERE ====================

    /**
     * 添加 WHERE 条件 (等值，字符串直接拼接，用于已知安全值)
     *
     * @param column 列名
     * @param value  值
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder whereEqLiteral(String column, String value) {
        appendCondition(column + " = '" + value + "'");
        return this;
    }

    /**
     * 添加 WHERE 条件 (数值等值，直接拼接)
     *
     * @param column 列名
     * @param value  数值
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder whereEqLiteral(String column, Number value) {
        appendCondition(column + " = " + value);
        return this;
    }

    /**
     * 添加 AND 条件
     *
     * @param condition 条件表达式
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder and(String condition) {
        if (hasWhereCondition) {
            whereClause.append(" AND ").append(condition);
        } else {
            appendCondition(condition);
        }
        return this;
    }

    /**
     * 添加 IN 条件 (字符串值直接拼接)
     *
     * @param column 列名
     * @param values 字符串值集合
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder andIn(String column, Collection<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return this;
        }
        StringBuilder inValues = new StringBuilder();
        for (String value : values) {
            if (inValues.length() > 0) {
                inValues.append(", ");
            }
            inValues.append("'").append(value).append("'");
        }
        and(column + " IN (" + inValues + ")");
        return this;
    }

    /**
     * 添加时间范围条件 (字符串字面量方式)
     *
     * @param column    时间列名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder andTimeRange(String column, String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            and(column + " >= '" + startTime + "'");
        }
        if (StringUtils.isNotBlank(endTime)) {
            and(column + " <= '" + endTime + "'");
        }
        return this;
    }

    /**
     * 添加时间范围条件 (字符串字面量方式，用于insert_time等字段)
     *
     * @param column    时间列名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder andTimeRangeLiteral(String column, String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            and(column + " >= '" + startTime + "'");
        }
        if (StringUtils.isNotBlank(endTime)) {
            and(column + " <= '" + endTime + "'");
        }
        return this;
    }

    /**
     * 添加 LIKE 条件
     *
     * @param column 列名
     * @param value  模糊匹配值 (不包含%)
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder andLike(String column, String value) {
        if (StringUtils.isNotBlank(value)) {
            and(column + " LIKE '%" + value + "%'");
        }
        return this;
    }

    // ==================== GROUP BY ====================

    /**
     * 设置 GROUP BY 子句
     *
     * @param columns 分组列
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder groupBy(String... columns) {
        groupByClause.setLength(0);
        groupByClause.append(" GROUP BY ").append(String.join(", ", columns));
        return this;
    }

    // ==================== ORDER BY ====================

    /**
     * 设置 ORDER BY 子句
     *
     * @param column 排序列
     * @param order  排序方向 (asc/desc)
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder orderBy(String column, String order) {
        orderByClause.setLength(0);
        orderByClause.append(" ORDER BY ").append(column).append(" ").append(order);
        return this;
    }

    // ==================== LIMIT ====================

    /**
     * 设置分页 LIMIT 子句
     *
     * @param offset 偏移量
     * @param size   每页大小
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder limit(int offset, int size) {
        limitClause.setLength(0);
        limitClause.append(" LIMIT ").append(offset).append(", ").append(size);
        return this;
    }

    /**
     * 设置 LIMIT BY 子句 (ClickHouse 特有)
     *
     * @param limit  每组限制数量
     * @param column 分组列
     * @return 当前构建器实例
     */
    public ClickhouseSqlBuilder limitBy(int limit, String column) {
        limitClause.setLength(0);
        limitClause.append(" LIMIT ").append(limit).append(" BY ").append(column);
        return this;
    }

    // ==================== 构建结果 ====================

    /**
     * 构建完整 SQL
     *
     * @return SQL 字符串
     */
    public String toSql() {
        return selectClause.toString()
                + fromClause.toString()
                + whereClause.toString()
                + groupByClause.toString()
                + orderByClause.toString()
                + limitClause.toString();
    }

    /**
     * 构建 COUNT SQL (用于分页总数查询)
     *
     * @return COUNT SQL 字符串
     */
    public String toCountSql() {
        return "SELECT COUNT(1)"
                + fromClause.toString()
                + whereClause.toString();
    }

    /**
     * 构建带 GROUP BY 的 COUNT SQL
     *
     * @return 带 GROUP BY 的 COUNT SQL 字符串
     */
    public String toGroupCountSql() {
        return "SELECT COUNT(1) FROM ("
                + "SELECT 1"
                + fromClause.toString()
                + whereClause.toString()
                + groupByClause.toString()
                + ") AS temp";
    }

    /**
     * 获取 WHERE 子句 (用于兼容旧代码)
     *
     * @return WHERE 子句字符串
     */
    public String getWhereClause() {
        return whereClause.toString();
    }

    // ==================== 内部方法 ====================

    /**
     * 追加条件到 WHERE 子句
     *
     * @param condition 条件表达式
     */
    protected void appendCondition(String condition) {
        if (!hasWhereCondition) {
            whereClause.append(" WHERE ").append(condition);
            hasWhereCondition = true;
        } else {
            whereClause.append(" AND ").append(condition);
        }
    }
}
