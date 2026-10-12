package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 工作任务标签(暂时不按租户自定义,内置初始化)
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkTag extends BasicEnhancedEntity<Long> {

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
