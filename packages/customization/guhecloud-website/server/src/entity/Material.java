package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属文件夹ID
     */
    private Long pathId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * 文件大小（字节）
     */
    private Integer size;

    /**
     * 存储路径/URL
     */
    private Long fileId;

}
