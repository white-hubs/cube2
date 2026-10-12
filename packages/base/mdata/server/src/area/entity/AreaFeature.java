package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 区域特征表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaFeature extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 特征名称
     */
    @NotNull(message = "名称不能为空")
    @Length(max = 300, min = 1, message = "长度为1-300")
    @TableField("`name`")
    private String name;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

}