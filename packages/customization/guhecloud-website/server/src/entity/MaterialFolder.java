package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材文件夹实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialFolder extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父文件夹ID，0表示根目录
     */
    private Long pathId;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
