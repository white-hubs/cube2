package com.guhecloud.cube.biz.ouaa.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagedOrganization extends BasicEnhancedEntity<Long> {
    private Long id;
    private Long userId;
    private Long organizationId;
}
