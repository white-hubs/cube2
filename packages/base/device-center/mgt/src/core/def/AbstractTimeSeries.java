package com.guhecloud.cube.iot.mgt.core.def;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * 时序查询抽象基类
 * <p>
 * 职责:
 * 1. 统一异常处理 (ClickHouse 表不存在等场景)
 * 2. 分页结果包装
 * 3. 默认时间范围处理
 */
@Slf4j
public abstract class AbstractTimeSeries implements TimeSeriesInterface {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    // ==================== 异常处理模板 ====================

    /**
     * 分页查询通用模板
     *
     * @param operationName 操作名称 (用于日志)
     * @param pageNumber    页码
     * @param pageSize      每页大小
     * @param executor      查询执行器
     * @return 分页结果
     */
    protected <T> IPage<T> executePageQuery(
            String operationName,
            int pageNumber,
            int pageSize,
            PageQueryExecutor<T> executor) {
        try {
            return executor.execute();
        } catch (Exception e) {
            if (canBeIgnored(e)) {
                log.warn("{} error, but can be ignored, e = {}", operationName, e.getMessage());
                return buildEmptyPage(pageNumber, pageSize);
            }
            throw e;
        }
    }

    /**
     * 列表查询通用模板
     *
     * @param executor 查询执行器
     * @return 列表结果
     */
    protected <T> List<T> executeListQuery(
            ListQueryExecutor<T> executor) {
        try {
            return executor.execute();
        } catch (Exception e) {
            if (canBeIgnored(e)) {
                log.warn("{} error, but can be ignored, e = {}", "queryMultiDevicePropHistory", e.getMessage());
                return Collections.emptyList();
            }
            throw e;
        }
    }

    /**
     * 判断异常是否可忽略 (ClickHouse 表不存在)
     *
     * @param e 异常
     * @return 是否可忽略
     */
    protected boolean canBeIgnored(Throwable e) {
        String msg = e.getMessage();
        if (msg == null) {
            return false;
        }

        return (msg.contains("Code: 60. DB::Exception: Table") && msg.contains("does not exist"))
                || (msg.contains("Code: 60, e.displayText() = DB::Exception: Table") && msg.contains("doesn't exist"));
    }

    // ==================== 分页工具 ====================

    /**
     * 构建空分页对象
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 空分页对象
     */
    protected <T> IPage<T> buildEmptyPage(int pageNumber, int pageSize) {
        IPage<T> page = new Page<>(pageNumber, pageSize);
        page.setRecords(Collections.emptyList());
        page.setTotal(0);
        return page;
    }

    /**
     * 包装分页结果
     *
     * @param records    记录列表
     * @param total      总数
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 分页对象
     */
    protected <T> IPage<T> wrapPage(List<T> records, long total, int pageNumber, int pageSize) {
        IPage<T> page = new Page<>(pageNumber, pageSize);
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }

    // ==================== 时间范围处理 ====================

    /**
     * 设置默认时间范围
     *
     * @param queryVo 实现 TimeRangeAware 的查询对象
     * @param hours   默认时间范围 (小时)
     */
    protected void applyDefaultTimeRange(TimeRangeAware queryVo, int hours) {
        if (queryVo == null) {
            return;
        }
        if (StringUtils.isBlank(queryVo.getStartTime()) && StringUtils.isBlank(queryVo.getEndTime())) {
            queryVo.setStartTime(LocalDateTime.now().minusHours(hours).format(DATE_TIME_FORMATTER));
            queryVo.setEndTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        }
    }

    // ==================== 函数式接口 ====================

    /**
     * 分页查询执行器
     */
    @FunctionalInterface
    protected interface PageQueryExecutor<T> {
        /**
         * 执行查询
         *
         * @return 分页结果
         */
        IPage<T> execute();
    }

    /**
     * 列表查询执行器
     */
    @FunctionalInterface
    protected interface ListQueryExecutor<T> {
        /**
         * 执行查询
         *
         * @return 列表结果
         */
        List<T> execute();
    }
}
