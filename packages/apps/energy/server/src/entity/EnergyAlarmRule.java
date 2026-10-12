package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 报警规则设备绑定
 * @date 2022-07-07 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyAlarmRule extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //报警规则id
    private Long rulesId;
    //设备id
    private String deviceId;

}