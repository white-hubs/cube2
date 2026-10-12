package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author appet
 * @Description 每日分项用量
 * @date 2021-12-30 15:12:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EnergyDailyUsageProject extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    private LocalDateTime statDate;
    private String instrumentType;
    private String instrumentTypeName;
    private String projectCategory;
    private Long projectId;
    private String projectCode;
    private String projectName;
    private BigDecimal dValue;

    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;
}