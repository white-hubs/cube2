package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class GroupAttributiveDetails extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 属性code
     */
    private String code;
    /**
     * 应用
     */
    private String applicationCode;
    /**
     * 组id
     */
    private Long groupId;
    //属性名称
    private String attributiveName;
}
