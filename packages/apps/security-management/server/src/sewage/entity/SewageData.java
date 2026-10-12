package com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SewageData extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String handlingMethod;
    private BigDecimal dataValue;
    private String area;
    private String areaId;
    private String deviceId;
    private String thirdPartyId;
    private String dataType;
    private LocalDateTime collectionTime;
}
