package com.guhecloud.platform.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.database.interceptor.DeletedTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础数据模型
 *
 * @param <T> 主键类型
 *
 * @author Woody
 * @date 2021/6/25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicEntity<T extends Serializable> implements Creatable<T>, Updatable<T>, Deletable<T> {
    /**
     * 是否已删除
     */
    @TableField(typeHandler = DeletedTypeHandler.class, keepGlobalFormat = true)
    private Boolean deleted;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新用户
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
