package com.guhecloud.platform.database.entity;

import com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor;

import java.io.Serializable;

/**
 * 树形数据模型，序列号自动初始化以及所有下级节点序列号的自动更新
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/5
 * @see BasicTreeEntity
 * @see BasicTenantTreeEntity
 * @see PropertyUpdateInterceptor
 */
public interface TreeEntity<T extends Serializable> extends Tree<T>, Entity<T> {
    /**
     * 获取序列号
     *
     * @return 序列号
     */
    String getSequence();

    /**
     * 设置序列号
     *
     * @param sequence 序列号
     */
    void setSequence(String sequence);
}
