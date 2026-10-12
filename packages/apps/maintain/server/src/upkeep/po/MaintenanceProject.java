package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保配置项
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceProject extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 维保项编码
     */
    private String code;
    /**
     * 维保项内容
     */
    private String maintenanceContent;

}