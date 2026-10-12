package com.guhecloud.cube.biz.todo.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 待办任务标签(暂时不按租户自定义,内置初始化)
 *
 * @author lanchun
 * @date 2023/2/27 11:23
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TodoTag extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 标签编号
     */
    @Unique(composites = { "deleted" }, message = "标签编号已存在")
    private String code;

    /**
     * 标签名称
     */
    @Unique(composites = { "deleted" }, message = "标签名称已存在")
    private String name;

    /**
     * 是否能移交
     */
    private Boolean handover;
}
