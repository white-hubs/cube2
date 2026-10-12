package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 产品（系统）数据模型
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Product extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品编号
     */
    @Unique(message = "产品编号已存在")
    private String code;

    /**
     * 产品名称
     */
    @Unique(composites = "deleted", message = "产品名称已存在")
    private String name;

    /**
     * 产品域名
     */
    private String domain;

    /**
     * 产品备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
