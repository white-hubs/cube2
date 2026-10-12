package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;

/**
 * 区域组信息
 *
 * @author lanchun
 * @date 2022/4/15 14:30
 */
@Data
public class AreaGroup extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域组名称
     */
    @Unique(composites = { "deleted" }, message = "区域组名称已存在")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
