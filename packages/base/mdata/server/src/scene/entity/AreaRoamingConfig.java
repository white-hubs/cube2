package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AreaRoamingConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 漫游名称
     */
    private String name;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String areas;

    /**
     * 漫游配置
     */
    private String roamingConfig;
}
