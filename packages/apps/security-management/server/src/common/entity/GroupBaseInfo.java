package com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class GroupBaseInfo extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String groupControlName;
    private String password;
    private Integer num;
    private String ascription;
}
