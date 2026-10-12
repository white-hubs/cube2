package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.database.entity.Isolatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 多租户的树形基础数据模型
 *
 * @param <T> 主键类型
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicEnhancedTenantTreeEntity<T extends Serializable> extends BasicEnhancedTreeEntity<T> implements Isolatable<T> {

    /**
     * 租户编号
     */
    private Integer tenant;
}
