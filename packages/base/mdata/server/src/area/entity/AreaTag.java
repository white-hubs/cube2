package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域标签
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaTag extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 标签名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 标签图标
     */
    private Long icoFileId;
}
