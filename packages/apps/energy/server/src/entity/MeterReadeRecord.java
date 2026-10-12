package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadeRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //读数
    private BigDecimal readeValue;
    //上期读数
    private BigDecimal readeLastValue;
    //上期用量
    private BigDecimal lastReadeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //本期时间
    private LocalDateTime nowReadeTime;
    //本期时间
    private LocalDateTime lastReadeTime;
    //设备id
    private String deviceId;

}
