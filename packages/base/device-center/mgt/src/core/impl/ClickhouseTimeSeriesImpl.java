package com.guhecloud.cube.iot.mgt.core.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.RuleTriggerLog;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsModelVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.LinkageEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.MultiDevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.def.AbstractTimeSeries;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.DeviceSamplingHistoryRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.MultiDevicePropRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.SingleDeviceEventRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.SingleDevicePropRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.SingleDeviceSchemaTipsRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.rowmapper.SingleLinkageEventLogRowMapper;
import com.guhecloud.cube.iot.mgt.core.impl.sql.EventQueryBuilder;
import com.guhecloud.cube.iot.mgt.core.impl.sql.LinkageQueryBuilder;
import com.guhecloud.cube.iot.mgt.core.impl.sql.PropQueryBuilder;
import com.guhecloud.cube.iot.mgt.core.impl.sql.SamplingPropQueryBuilder;
import com.guhecloud.cube.iot.mgt.core.impl.sql.SchemaQueryBuilder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClickHouse 时序数据查询实现类
 */
@Slf4j
public class ClickhouseTimeSeriesImpl extends AbstractTimeSeries implements TimeSeriesInterface {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时间范围：30天
     */
    private static final int DEFAULT_TIME_RANGE_HOURS = 24 * 30;

    /**
     * 事件默认时间范围：2小时
     */
    private static final int EVENT_TIME_RANGE_HOURS = 2;

    private final JdbcTemplate jdbcTemplate;

    public ClickhouseTimeSeriesImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ==================== 单设备属性历史查询 ====================

    @Override
    public IPage<DevicePropHistoryResultVo> querySingleDevicePropPage(
            DevicePropHistoryQueryVo queryVO,
            Integer pageNumber,
            Integer pageSize) {

        return executePageQuery("querySingleDevicePropPage", pageNumber, pageSize, () -> {
            applyDefaultTimeRange(queryVO, DEFAULT_TIME_RANGE_HOURS);
            String tableName = buildDevicePropTableName(queryVO.getTenantId());

            PropQueryBuilder builder = createSingleDevicePropBuilder(queryVO, tableName);
            builder.orderBy("ts", queryVO.getOrder())
                    .limit((pageNumber - 1) * pageSize, pageSize);

            log.info("querySingleDevicePropPage sql ---> [{}]", builder.toSql());

            List<DevicePropHistoryResultVo> records = jdbcTemplate.query(
                    builder.toSql(), new SingleDevicePropRowMapper());

            long total = queryPropCount(builder, queryVO.getAggType());

            return wrapPage(records, total, pageNumber, pageSize);
        });
    }

    /**
     * 创建单设备属性查询 Builder
     *
     * @param queryVO   查询条件
     * @param tableName 表名
     * @return PropQueryBuilder
     */
    private PropQueryBuilder createSingleDevicePropBuilder(DevicePropHistoryQueryVo queryVO, String tableName) {
        PropQueryBuilder builder = new PropQueryBuilder().from(tableName);

        if ("raw".equals(queryVO.getAggType())) {
            builder.selectRawColumns()
                    .withProductId(queryVO.getProductId());
        } else {
            builder.selectSamplingColumns(queryVO.getAggType(), queryVO.getStep())
                    .groupBySampling(queryVO.getStep());
        }

        builder.withDeviceId(queryVO.getDeviceId())
                .withTimeRange(queryVO.getStartTime(), queryVO.getEndTime())
                .withPropCodes(queryVO.getPropCodes());

        return builder;
    }

    /**
     * 查询属性记录总数
     *
     * @param builder 已构建的 SQL Builder
     * @param aggType 聚合类型
     * @return 记录总数
     */
    private long queryPropCount(PropQueryBuilder builder, String aggType) {
        String countSql;

        if ("raw".equals(aggType)) {
            countSql = builder.toCountSql();
        } else {
            countSql = builder.toGroupCountSql();
        }

        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return count == null ? 0 : count;
    }

    // ==================== 多设备属性历史查询 ====================

    @Override
    public List<DevicePropHistoryResultVo> queryMultiDevicePropHistory(
            MultiDevicePropHistoryQueryVo queryVO) {

        return executeListQuery(() -> {
            if (queryVO == null || CollectionUtils.isEmpty(queryVO.getItems())) {
                return Collections.emptyList();
            }

            applyDefaultTimeRange(queryVO, DEFAULT_TIME_RANGE_HOURS);
            String tableName = buildDevicePropTableName(queryVO.getTenantId());

            PropQueryBuilder builder = createMultiDevicePropBuilder(queryVO, tableName);
            builder.orderBy("ts", queryVO.getOrder())
                    .limitBy(queryVO.getMaxRecords(), "device_id");

            log.info("queryMultiDevicePropHistory sql ---> [{}]", builder.toSql());

            return jdbcTemplate.query(builder.toSql(), new MultiDevicePropRowMapper());
        });
    }

    /**
     * 创建多设备属性查询 Builder
     *
     * @param queryVO   查询条件
     * @param tableName 表名
     * @return PropQueryBuilder
     */
    private PropQueryBuilder createMultiDevicePropBuilder(MultiDevicePropHistoryQueryVo queryVO, String tableName) {
        PropQueryBuilder builder = new PropQueryBuilder().from(tableName);

        if ("raw".equals(queryVO.getAggType())) {
            builder.selectRawColumnsWithDevice()
                    .withDevicePropTuples(queryVO.getItems(), true);
        } else {
            builder.selectSamplingColumns(queryVO.getAggType(), queryVO.getStep())
                    .withDevicePropTuples(queryVO.getItems(), false)
                    .groupBySampling(queryVO.getStep());
        }

        builder.withTimeRange(queryVO.getStartTime(), queryVO.getEndTime());

        return builder;
    }

    // ==================== 设备采样属性查询 ====================

    @Override
    public IPage<DeviceSamplingHistoryResultVo> queryDeviceSamplingPropPage(
            DeviceSamplingHistoryQueryVo queryVO,
            Integer pageNumber,
            Integer pageSize) {

        String tableName = buildSamplingViewTableName(queryVO.getTenantId());
        IPage<DeviceSamplingHistoryResultVo> iPage = new Page<>(pageNumber, pageSize);

        if (!checkViewExist(tableName)) {
            iPage.setRecords(new ArrayList<>());
            iPage.setTotal(0);
            return iPage;
        }

        SamplingPropQueryBuilder builder = createSamplingPropBuilder(queryVO, tableName);

        // 查询总数
        Integer total = jdbcTemplate.queryForObject(builder.toDistinctCountSql(), Integer.class);

        // 添加分组、排序和分页
        builder.groupByDeviceAndTs()
                .orderBy("ts", queryVO.getOrder())
                .limit((pageNumber - 1) * pageSize, pageSize);

        log.info("queryDeviceSamplingPropPage sql ---> [{}]", builder.toSql());

        List<DeviceSamplingHistoryResultVo> list = jdbcTemplate.query(
                builder.toSql(), new DeviceSamplingHistoryRowMapper());

        iPage.setRecords(list);
        iPage.setTotal(total == null ? 0 : total);

        return iPage;
    }

    /**
     * 创建采样属性查询 Builder
     *
     * @param queryVO   查询条件
     * @param tableName 表名
     * @return SamplingPropQueryBuilder
     */
    private SamplingPropQueryBuilder createSamplingPropBuilder(DeviceSamplingHistoryQueryVo queryVO, String tableName) {
        return new SamplingPropQueryBuilder()
                .selectSamplingColumns()
                .from(tableName)
                .withProductId(queryVO.getProductId())
                .withDeviceId(queryVO.getDeviceId())
                .withPropCodes(queryVO.getPropCodes())
                .withTimeRange(queryVO.getStartTime(), queryVO.getEndTime());
    }

    /**
     * 构建采样视图表名
     *
     * @param tenantId 租户ID
     * @return 采样视图表名
     */
    private String buildSamplingViewTableName(String tenantId) {
        return "iotp_ts_2.tenant_device_props_" + tenantId + "_min_10_any_view";
    }

    // ==================== 单设备事件历史查询 ====================

    @Override
    public IPage<DeviceEventHistoryResultVo> querySingleDeviceEventPage(
            DeviceEventHistoryQueryVo queryVO,
            Integer pageNumber,
            Integer pageSize) {

        return executePageQuery("querySingleDeviceEventPage", pageNumber, pageSize, () -> {
            applyDefaultTimeRange(queryVO, EVENT_TIME_RANGE_HOURS);
            String tableName = "guhe_iot_bz_2.iot_product_event";

            EventQueryBuilder builder = new EventQueryBuilder()
                    .selectEventColumns()
                    .from(tableName)
                    .withDeviceId(queryVO.getDeviceId())
                    .withBizType(queryVO.getBizType())
                    .withTimeRange(queryVO.getStartTime(), queryVO.getEndTime())
                    .withEventCodes(queryVO.getEventCodes());

            builder.orderBy("insert_time", queryVO.getOrder())
                    .limit((pageNumber - 1) * pageSize, pageSize);

            log.info("event history sql ---> [{}]", builder.toSql());

            List<DeviceEventHistoryResultVo> records = jdbcTemplate.query(
                    builder.toSql(), new SingleDeviceEventRowMapper());

            Integer count = jdbcTemplate.queryForObject(builder.toCountSql(), Integer.class);
            long total = count == null ? 0 : count;

            return wrapPage(records, total, pageNumber, pageSize);
        });
    }

    // ==================== 联动规则触发日志查询 ====================

    @Override
    public IPage<RuleTriggerLog> querySingleLinkagePage(LinkageEventHistoryQueryVo param) {
        return executePageQuery("querySingleLinkagePage", param.getPageNumber(), param.getPageSize(), () -> {
            String tableName = "guhe_iot_bz_2.iot_linkage_event";

            LinkageQueryBuilder builder = new LinkageQueryBuilder()
                    .selectAll()
                    .from(tableName)
                    .withTimeRange(param.getStartTs(), param.getEndTs())
                    .withRuleId(param.getRuleNum())
                    .withInstanceId(param.getInstanceId())
                    .withTenantId(param.getTenantId());

            builder.orderBy("insert_time", "desc")
                    .limit((param.getPageNumber() - 1) * param.getPageSize(), param.getPageSize());

            log.info("linkage event history sql ---> [{}]", builder.toSql());

            List<RuleTriggerLog> records = jdbcTemplate.query(
                    builder.toSql(), new SingleLinkageEventLogRowMapper());

            Integer count = jdbcTemplate.queryForObject(builder.toCountSql(), Integer.class);
            long total = count == null ? 0 : count;

            return wrapPage(records, total, param.getPageNumber(), param.getPageSize());
        });
    }

    // ==================== 设备 Schema 信息查询 ====================

    @Override
    public IPage<DeviceSchemaTipsModelVo> queryDeviceSchemaInfoPage(DeviceSchemaTipsQueryVo queryVO) {
        // 处理 maxRecords 参数
        if (queryVO.getPageSize() <= 0) {
            queryVO.setPageSize(queryVO.getMaxRecords());
        }

        // 默认当天数据
        if (StringUtils.isBlank(queryVO.getStartTime()) && StringUtils.isBlank(queryVO.getEndTime())) {
            queryVO.setStartTime(DateUtils.format(DateUtils.beginOfDay(LocalDateTime.now()), DEFAULT_DATE_TIME_FORMAT));
            queryVO.setEndTime(DateUtils.format(DateUtils.endOfDay(LocalDateTime.now()), DEFAULT_DATE_TIME_FORMAT));
        }

        String tableName = "guhe_iot_bz_2.iot_schema_device_with_rmt";

        SchemaQueryBuilder builder = new SchemaQueryBuilder()
                .selectSchemaColumns()
                .from(tableName, true)
                .withTimeRange(queryVO.getStartTime(), queryVO.getEndTime())
                .withTenantId(queryVO.getTenantId())
                .withDevNumLike(queryVO.getDevNumLike())
                .withDevNameLike(queryVO.getDevNameLike())
                .withParentDevNum(queryVO.getParentDevNum())
                .withOnlyParent(queryVO.isOnlyParent());

        // 统计总数
        Integer count;
        try {
            count = jdbcTemplate.queryForObject(builder.toCountSql(), Integer.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("does not exist")) {
                log.warn("queryDeviceSchemaInfoPage table [{}] not exist, return empty result", tableName);
                return new Page<>(queryVO.getPageNumber(), queryVO.getPageSize(), 0);
            }
            log.error("queryDeviceSchemaInfoPage error", e);
            throw e;
        }

        // 分页查询
        builder.limit((int) ((queryVO.getPageNumber() - 1) * queryVO.getPageSize()), (int) queryVO.getPageSize());
        List<DeviceSchemaTipsModelVo> list = jdbcTemplate.query(builder.toSql(), new SingleDeviceSchemaTipsRowMapper());

        log.info("queryDeviceSchemaInfoPage sql ---> [{}]", builder.toSql());

        IPage<DeviceSchemaTipsModelVo> iPage = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        iPage.setRecords(list);
        iPage.setTotal(Objects.isNull(count) ? 0 : count);

        return iPage;
    }

    // ==================== 工具方法 ====================

    /**
     * 构建设备属性表名
     *
     * @param tenantId 租户ID
     * @return 表名
     */
    public String buildDevicePropTableName(String tenantId) {
        return "iotp_ts_2.tenant_device_props_" + tenantId;
    }

    /**
     * 检查视图是否存在
     *
     * @param tableName 表名
     * @return 是否存在
     */
    private boolean checkViewExist(String tableName) {
        String[] split = tableName.split("\\.");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "SELECT * FROM system.tables WHERE database = '" + split[0] + "' AND name = '" + split[1] + "' AND engine LIKE '%View%';");
        return ObjectUtils.isNotEmpty(maps);
    }
}
