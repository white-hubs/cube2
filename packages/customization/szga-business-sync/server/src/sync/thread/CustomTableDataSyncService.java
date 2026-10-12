package com.guhecloud.cube.srv.gen.business.info.sync.server.sync.thread;

import com.guhecloud.cube.srv.gen.business.info.sync.server.service.dto.CustomTableDataSyncParam;
import com.guhecloud.platform.base.util.DateUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/8 14:43
 * @Description: 数据同步线程
 */
@Slf4j
public class CustomTableDataSyncService {
    public static final List<String> syncTableName = Arrays.asList(
            "visit_area", "visitor_invite_record",
            "reservation_record", "event_record");
    public static final Map<String, String> tableModelMap = new HashMap<>();

    static {
        tableModelMap.put("visit_area", "cube_srv_gen_adapter_third.visit_area");
        tableModelMap.put("visitor_invite_record", "cube_srv_gen_adapter_third.visitor_invite_record");
        tableModelMap.put("reservation_record", "cube_srv_gen_adapter_third.reservation_record");
        tableModelMap.put("event_record", "cube_base_event.event_record");
    }

    private volatile boolean toStop = false;

    private static final String LAST_CONSUMER_TIME_STAMP = "LAST_UPDATE_TIME_STAMP";
    private static final String LAST_PRODUCER_TIME_STAMP = "LAST_INSERT_TIME_STAMP";

    private final RedisTemplate<String, Object> redisTemplate;

    private final JdbcTemplate sourceJdbcTemplate;

    private final JdbcTemplate targetJdbcTemplate;

    private final TransactionTemplate sourceTransactionTemplate;

    private final TransactionTemplate targetTransactionTemplate;

    public CustomTableDataSyncService(RedisTemplate<String, Object> redisTemplate, HikariDataSource sourceDataSource,
                                      HikariDataSource targetDataSource
                                     ) {
        this.redisTemplate = redisTemplate;
        this.sourceJdbcTemplate = new JdbcTemplate(sourceDataSource);
        this.targetJdbcTemplate = new JdbcTemplate(targetDataSource);

        // 初始化事务模板
        this.sourceTransactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(sourceDataSource));
        this.targetTransactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(targetDataSource));
    }

    private boolean remove = false;

    public void executeDataSync(CustomTableDataSyncParam param) {
        try {
            for (String tableName : syncTableName) {
                String timestampColumn = "update_time";
                if ("event_record".equalsIgnoreCase(tableName)) {
                    timestampColumn = "create_time";
                }
                //增量消费
                incrementalConsumerWithPool(tableName, "id", timestampColumn, param);
                //增量生产
                incrementalProducerWithPool(tableName, "id", timestampColumn);
            }
        } catch (Exception e) {
            if (!toStop) {
                log.error("table data sync fail", e);
            }
        }
    }

    /**
     * 增量生产数据到网闸（基于主键比较，使用连接池）
     *
     * @param tableName       表名
     * @param keyColumn       主键列名
     * @param timestampColumn 时间戳列名（用于增量判断）
     */
    private void incrementalProducerWithPool(String tableName, String keyColumn, String timestampColumn) {

        sourceTransactionTemplate.execute(status -> {
            try {
                LocalDateTime lastSyncTime = null;
                Object o = this.redisTemplate.opsForHash().get(LAST_PRODUCER_TIME_STAMP, tableName);
                if (Objects.nonNull(o)) {
                    lastSyncTime = (LocalDateTime) o;
                } else {
                    // 1. 获取目标表最后同步时间
                    lastSyncTime = sourceJdbcTemplate.queryForObject(
                            "SELECT MAX(" + timestampColumn + ") FROM " + tableName, LocalDateTime.class);
                }
                if (lastSyncTime == null) {
                    lastSyncTime = LocalDateTime.now();
                }

                // 2. 获取源表变更数据
                List<Map<String, Object>> changedData = targetJdbcTemplate.queryForList(
                        "SELECT * FROM " + tableModelMap.get(tableName) + " WHERE " + timestampColumn + " > ?",
                        lastSyncTime);

                // 3. 同步变更数据
                if (!changedData.isEmpty()) {
                    // 使用达梦的MERGE语法实现UPSERT
                    String mergeSql = buildMergeSql(tableName, keyColumn, changedData.get(0).keySet());
                    // 执行MERGE操作
                    sourceJdbcTemplate.batchUpdate(mergeSql, changedData.stream()
                            .map(row -> columnsToArray(row, row.keySet().toArray(new String[0])))
                            .collect(Collectors.toList()));
                    var date = changedData.stream().map(map -> DateUtils.parse(map.get(timestampColumn).toString()))
                            .max(Comparator.comparing(t -> t))
                            .get();
                    this.redisTemplate.opsForHash().put(LAST_PRODUCER_TIME_STAMP, tableName, date);
                }
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("Incremental sync failed", e);
            }
        });
    }

    public void toStop() {
        this.toStop = true;
    }

    /**
     * 增量消费网闸数据（基于主键比较，使用连接池）
     *
     * @param tableName       表名
     * @param keyColumn       主键列名
     * @param timestampColumn 时间戳列名（用于增量判断）
     * @param param
     */
    public void incrementalConsumerWithPool(String tableName, String keyColumn, String timestampColumn, CustomTableDataSyncParam param) {

        targetTransactionTemplate.execute(status -> {
            try {
                LocalDateTime lastSyncTime = null;
                Object o = this.redisTemplate.opsForHash().get(LAST_CONSUMER_TIME_STAMP, tableName);
                if (Objects.nonNull(o)) {
                    lastSyncTime = (LocalDateTime) o;
                } else {
                    // 1. 获取目标表最后同步时间
                    lastSyncTime = targetJdbcTemplate.queryForObject(
                            "SELECT MAX(" + timestampColumn + ") FROM " + tableModelMap.get(tableName), LocalDateTime.class);
                }
                if (lastSyncTime == null) {
                    lastSyncTime = LocalDateTime.now();
                }

                // 2. 获取源表变更数据
                List<Map<String, Object>> changedData = sourceJdbcTemplate.queryForList(
                        "SELECT * FROM " + tableName + " WHERE " + timestampColumn + " > ?",
                        lastSyncTime);

                // 3. 同步变更数据
                if (!changedData.isEmpty()) {
                    mergeData(tableModelMap.get(tableName), keyColumn, changedData);
                    var date = changedData.stream().map(map -> DateUtils.parse(map.get(timestampColumn).toString()))
                            .max(Comparator.comparing(t -> t))
                            .get();
                    // 4. 清理网闸数据
                    if (Objects.nonNull(param) && Boolean.TRUE.equals(param.getRemove())) {
                        var removeDate =
                                Objects.nonNull(param.getRemoveTime()) ? DateUtils.offsetDay(LocalDateTime.now(), -param.getRemoveTime())
                                                                       : DateUtils.offsetDay(LocalDateTime.now(), -30);
                        //删除源数据库数据
                        sourceTransactionTemplate.execute(sourceStatus -> {
                            try {
                                sourceJdbcTemplate.update("DELETE FROM " + tableName + " WHERE " + timestampColumn + " <= ?", removeDate);
                                return true;
                            } catch (Exception e) {
                                sourceStatus.setRollbackOnly();
                                throw new RuntimeException("Incremental sync failed", e);
                            }
                        });
                    }
                    this.redisTemplate.opsForHash().put(LAST_CONSUMER_TIME_STAMP, tableName, date);
                }
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("Incremental sync failed", e);
            }
        });
    }

    private void mergeData(String tableName, String keyColumn, List<Map<String, Object>> data) {
        // 使用达梦的MERGE语法实现UPSERT
        String mergeSql = buildMergeSql(tableName, keyColumn, data.get(0).keySet());

        // 执行MERGE操作
        targetJdbcTemplate.batchUpdate(mergeSql, data.stream()
                .map(row -> columnsToArray(row, row.keySet().toArray(new String[0])))
                .collect(Collectors.toList()));
    }

    private String buildMergeSql(String tableName, String keyColumn, Set<String> columns) {
        StringBuilder sql = new StringBuilder("MERGE INTO ")
                .append(tableName).append(" T USING (SELECT ");

        // 构建VALUES子查询
        columns.forEach(col -> sql.append("? AS ").append(col).append(","));
        sql.deleteCharAt(sql.length() - 1)
                .append(" FROM DUAL) S ON (T.").append(keyColumn).append(" = S.").append(keyColumn).append(")")
                .append(" WHEN MATCHED THEN UPDATE SET ");

        // 构建UPDATE SET部分
        columns.stream()
                .filter(col -> !col.equalsIgnoreCase(keyColumn))
                .forEach(col -> sql.append("T.").append(col).append(" = S.").append(col).append(","));
        sql.deleteCharAt(sql.length() - 1);

        // 构建INSERT部分
        sql.append(" WHEN NOT MATCHED THEN INSERT (")
                .append(String.join(",", columns))
                .append(") VALUES (")
                .append(columns.stream().map(col -> "S." + col).collect(Collectors.joining(",")))
                .append(")");

        return sql.toString();
    }

    private Object[] columnsToArray(Map<String, Object> row, String[] columns) {
        return Arrays.stream(columns)
                .map(row::get)
                .toArray();
    }
}
