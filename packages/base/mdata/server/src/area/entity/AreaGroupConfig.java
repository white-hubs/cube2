package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域组下的区域
 *
 * @author lanchun
 * @date 2022/4/18 14:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaGroupConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 区域组主键
     */
    @Unique(composites = { "areaId", "deleted" }, message = "区域组已关联该区域")
    private Long areaGroupId;

    /**
     * 区域组序列号
     */
    private String areaGroup;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域序列号
     */
    private String area;

}
