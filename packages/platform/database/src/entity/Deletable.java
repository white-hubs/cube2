package com.guhecloud.platform.database.entity;

import com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor;

import java.io.Serializable;

/**
 * 可设置删除标记的数据模型，删除标记自动初始化以及数据查询自动过滤
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/2
 * @see BasicEntity
 * @see PropertyUpdateInterceptor
 */
public interface Deletable<T extends Serializable> extends Entity<T> {
    /**
     * 判断是否已删除
     *
     * @return true/false
     */
    Boolean getDeleted();

    /**
     * 设置是否已删除
     *
     * @param deleted true/false
     */
    void setDeleted(Boolean deleted);
}
