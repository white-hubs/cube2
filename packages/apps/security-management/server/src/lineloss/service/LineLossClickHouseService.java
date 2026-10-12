package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

public interface LineLossClickHouseService {

    <T> Pagination<T> pageClickHouse(String tableName, Class<T> clazz, String startTime, String endTime, Set<Long> deviceIdSet,
                                     long pageNumber,
                                     long pageSize,
                                     String orderByField
                                    );

    <T> List<T> listClickHouse(String tableName, Class<T> clazz, String startTime, String endTime, Set<Long> deviceIdSet,
                               String orderByField
                              );

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
    <T> List<T> listClickHouse(String tableName, Class<T> clazz, QueryCondition queryCondition, String orderByField);

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
    <T> Pagination<T> pageClickHouse(String tableName, Class<T> clazz, QueryCondition queryCondition,
                                     String orderByField, long pageNumber, long pageSize
                                    );

    <T> int batchInsertClickHouse(String tableName, List<T> dataList);

    <T> List<T> listLatestByWhereColumn(String tableName, Class<T> clazz, Set<Long> whereIdSet, String column);
}
