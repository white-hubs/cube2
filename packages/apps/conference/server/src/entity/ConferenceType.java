package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class ConferenceType extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private Boolean status;
}
