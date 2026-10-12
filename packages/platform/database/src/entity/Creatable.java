package com.guhecloud.platform.database.entity;

import com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 可设置创建信息的数据模型，创建信息自动初始化
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/2
 * @see BasicEntity
 * @see PropertyUpdateInterceptor
 */
public interface Creatable<T extends Serializable> extends Entity<T> {
    /**
     * 获取创建用户
     *
     * @return 创建用户
     */
    String getCreateBy();

    /**
     * 设置创建用户
     *
     * @param createBy 创建用户
     */
    void setCreateBy(String createBy);

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    LocalDateTime getCreateTime();

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    void setCreateTime(LocalDateTime createTime);
}
