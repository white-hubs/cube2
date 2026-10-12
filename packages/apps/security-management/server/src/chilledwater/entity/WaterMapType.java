package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class WaterMapType extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //映射code
    private String mapCode;
    //品类名称
    private String deviceProdCategoryName;
    //品类code
    private String deviceProdCategoryCode;
}
