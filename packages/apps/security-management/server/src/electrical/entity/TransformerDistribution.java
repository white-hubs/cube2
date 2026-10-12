package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class TransformerDistribution extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long transformer;
    private String transformerName;
    private Long highDistribution;
    private String highDistributionName;
}
