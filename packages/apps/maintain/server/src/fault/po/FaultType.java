package com.guhecloud.cube.srv.gen.maintain.server.fault.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/5/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaultType extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
    private String applicationCode;
}
