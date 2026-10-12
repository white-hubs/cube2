package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 组织机构数据模型
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
public class Organization extends BasicEnhancedTenantTreeEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 组织机构名称
     */
    @Unique(message = "组织机构名称已存在")
    private String name;

    /**
     * 组织机构图标
     */
    private String icon;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 组织机构备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 扩展字段（JSON）
     */
    private String extension;
    /**
     * 上级组织主键
     */
    private Long parentId;
}
