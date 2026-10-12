package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class ClodWaterAttributeMap extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //属性code
    private String attributeCode;
    //翻译值
    private String translateName;

    private String valueType;
}
