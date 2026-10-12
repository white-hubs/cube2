package com.guhecloud.cube.srv.gen.content.management.server.announcement.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModelType extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //模块类型
    private String modelType;
    //类型名称
    private String typeName;
    //一级类型
    private String oneLevelType;
}
