package com.guhecloud.platform.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 树形对象接口
 *
 * @param <T> 主键类型
 * @param <M> 子节点类型
 *
 * @author Woody
 * @date 2021/7/5
 */
public interface Tree<T, M extends Tree<T, M>> extends Serializable {
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

    /**
     * 获取子节点列表
     *
     * @return 子节点列表
     */
    List<M> getChildren();

    /**
     * 设置子节点列表
     *
     * @param children 子节点列表
     */
    void setChildren(List<M> children);

    /**
     * 树形接口简单实现
     *
     * @param <T> 主键类型
     * @param <M> 子节点类型
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    class Simple<T, M extends Simple<T, M>> implements Tree<T, M> {
        /**
         * 主键
         */
        private T id;

        /**
         * 上级主键
         */
        private T parentId;

        /**
         * 树层级
         */
        private Integer level;

        /**
         * 子节点列表
         */
        private List<M> children;
    }
}
