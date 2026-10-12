package com.guhecloud.platform.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 多租户基础数据模型
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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicTenantEntity<T extends Serializable> extends BasicEntity<T> implements Isolatable<T> {
    /**
     * 租户编号
     */
    private Integer tenant;
}
