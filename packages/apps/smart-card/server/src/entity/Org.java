package com.guhecloud.cube.biz.smartcard.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 组织信息
 */
@Data
public class Org extends BasicEnhancedTenantEntity<String> {
    private String id;
    private String orgCode;
    private String orgName;
    private String parentId;
    private String orgPath;//例如：默认部门/xfj的组织
}
