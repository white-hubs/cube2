package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 能耗分析开关
 * @date 2022-07-04 15:12:36
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class EnergyGate extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //能耗类型
    private String energyType;
    //开关
    private Boolean open;

}