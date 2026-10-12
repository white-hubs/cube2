package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 角色数据模型
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
public class Role extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品主键
     */
    @Deprecated
    private Long productId;

    /**
     * 角色名称
     */
    @Unique(composites = "productId", message = "角色名称已存在")
    private String name;

    /**
     * 角色图标
     */
    private String icon;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 是否内置
     */
    private Boolean builtin;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
