package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 费用配置
 * @date 2022-07-04 15:12:36
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class EnergyChargeConfig extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //仪表类型
    private String instrumentType;
    //收费类型
    private String chargeType;
    //状态 是否启用
    private Boolean status;
    //jsonConfig
    private String jsonConfig;

}