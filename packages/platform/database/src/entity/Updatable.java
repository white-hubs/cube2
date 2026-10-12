package com.guhecloud.platform.database.entity;

import com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 可设置更新信息的数据模型，更新信息自动初始化
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/7/2
 * @see BasicEntity
 * @see PropertyUpdateInterceptor
 */
public interface Updatable<T extends Serializable> extends Entity<T> {
    /**
     * 获取更新用户
     *
     * @return 更新用户
     */
    String getUpdateBy();

    /**
     * 设置更新用户
     *
     * @param updateBy 更新用户
     */
    void setUpdateBy(String updateBy);

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    LocalDateTime getUpdateTime();

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    void setUpdateTime(LocalDateTime updateTime);
}
