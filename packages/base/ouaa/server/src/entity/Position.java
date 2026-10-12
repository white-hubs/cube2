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
 * 岗位数据模型
 *
 * @author Woody
 * @date 2021/7/2
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Position extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 岗位名称
     */
    @Unique(message = "职位名称已存在")
    private String name;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 岗位备注
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
}
