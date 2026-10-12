package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保对象
 * @date 2022-4-29 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceObject extends BasicEnhancedTenantEntity<Long> {

    /**
     * id
     */
    private Long id;
    //前端key
    private Long keyId;
    /**
     * 对象名称
     */
    private String objectName;
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
     * 区域
     */
    private String area;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 安装地址
     */
    private String installationAddress;

}