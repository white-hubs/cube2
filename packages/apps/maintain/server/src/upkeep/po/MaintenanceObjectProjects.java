package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保对象维保项
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceObjectProjects extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    /**
     * 结果项项id
     */
    private Long projectId;
    /**
     * 维保对象
     */
    private Long objectId;

}