package com.guhecloud.platform.database.entity;

import java.io.Serializable;

/**
 * 数据模型接口
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/11
 */
public interface Entity<T extends Serializable> extends Serializable {
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
}
