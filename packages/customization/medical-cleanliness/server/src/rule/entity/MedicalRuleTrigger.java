package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRuleTrigger extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * iot实例主键
     */
    private String iotInstanceId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 触发设备id
     */
    private Long deviceId;

    /**
     * 触发设备名称
     */
    private String deviceName;

    /**
     * 触发部门主键
     */
    private Long departId;

    /**
     * 触发部门名称
     */
    private String departName;
}
