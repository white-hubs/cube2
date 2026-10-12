package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保结果记录
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceProgetRecord extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 维保项
     */
    private String maintrenaceProject;
    /**
     * 维保记录
     */
    private String record;
    /**
     * 维保描述
     */
    private String maintenanceDescribe;

    /**
     * 维保人
     */
    private String maintenancePerson;

    /**
     * 维保人id
     */
    private Long maintenancePersonId;

}