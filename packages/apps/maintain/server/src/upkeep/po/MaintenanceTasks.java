package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 维保任务
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceTasks extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 对象id
     */
    private Long objectId;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 任务状态
     */
    private String maintenanceTaskStatus;
    /**
     * 维保发布时间
     */
    private String maintenanceTime;
    /**
     * 维保完成时间
     */
    private LocalDateTime maintenanceFinishTime;

    //编码
    private String code;
    /**
     * 图片
     */
    private String picture;
    /**
     * 描述
     */
    private String taskRemark;
    /**
     * 维保人员
     */
    private String maintenanceTaskPerson;
    /**
     * 维保人员id
     */
    private Long maintenanceTaskPersonId;

}