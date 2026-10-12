package com.guhecloud.platform.database.entity;

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
 * @param <M> 子节点类型
 *
 * @author Woody
 * @date 2021/7/6
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicTreeEntity<T extends Serializable>
        extends BasicEntity<T> implements TreeEntity<T> {
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
