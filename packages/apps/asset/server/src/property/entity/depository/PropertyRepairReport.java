package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-28  11:22
 * @Description:
 * @Version: 1.0
 */
@Data
public class PropertyRepairReport extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String processInstanceId;
    private String propertyStatus;
    private String status;
    private Long propertyId;
}
