package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.database.entity.TreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 树形基础数据模型
 *
 * @param <T> 主键类型
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicEnhancedTreeEntity<T extends Serializable> extends BasicEnhancedEntity<T> implements TreeEntity<T> {

    /**
     * 上级主键
     */
    private T parentId;

    /**
     * 树层级
     */
    private Integer level;

    /**
     * 序列号
     */
    private String sequence;
}
