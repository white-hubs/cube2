package com.guhecloud.platform.database.entity;

import java.io.Serializable;

/**
 * 树形对象接口
 *
 * @param <T> 主键类型
 */
public interface Tree<T> extends Serializable {
    /**
     * 获取主键
     *
     * @return 主键
     */
    T getId();

    /**
     * 设置主键
     *
     * @param id 主键
     */
    void setId(T id);

    /**
     * 获取上级主键
     *
     * @return 主键
     */
    T getParentId();

    /**
     * 设置上级主键
     *
     * @param parentId 上级主键
     */
    void setParentId(T parentId);

    /**
     * 获取树层级
     *
     * @return 层级
     */
    Integer getLevel();

    /**
     * 设置树层级
     *
     * @param level 层级
     */
    void setLevel(Integer level);
}
