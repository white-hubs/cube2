package com.guhecloud.cube.srv.gen.maintain.server.fault.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaultCase extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 故障案例名称
     */
    private String name;

    /**
     * 故障类型主键
     */
    private Long faultTypeId;

    /**
     * 故障类型名称
     */
    private String faultTypeName;

    /**
     * 设备类型主键
     */
    private Long deviceTypeId;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 故障现象
     */
    private String faultPhenomenon;

    /**
     * 故障原因
     */
    private String faultReason;

    /**
     * 处理措施
     */
    private String treatmentMeasures;

    /**
     * 产品品类
     */
    private String productCategoryCode;
}
