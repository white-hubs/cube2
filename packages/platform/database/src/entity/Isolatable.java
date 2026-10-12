package com.guhecloud.platform.database.entity;

import com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor;

import java.io.Serializable;

/**
 * 可进行数据隔离的数据模型（多租户），租户信息自动初始化以及数据查询自动过滤
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/3
 * @see BasicTenantEntity
 * @see BasicTenantTreeEntity
 * @see PropertyUpdateInterceptor
 */
public interface Isolatable<T extends Serializable> extends Entity<T> {
    /**
     * 获取租户编号
     *
     * @return 租户编号
     */
    Integer getTenant();

    /**
     * 设置租户编号
     *
     * @param tenant 租户编号
     */
    void setTenant(Integer tenant);
}
